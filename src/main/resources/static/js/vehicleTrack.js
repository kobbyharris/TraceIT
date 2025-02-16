mapboxgl.accessToken = 'pk.eyJ1IjoiNTVoYXJyeSIsImEiOiJjbTV2M2U5emIwMmVnMmlzOTEzYm9zcG05In0.IFJCmaZz8bMJevo4eWtgFw';

const map = new mapboxgl.Map({
    container: 'map',
    style: 'mapbox://styles/mapbox/streets-v12',
    zoom: 10,
    center: [-1.7554, 4.8932]
});

map.addControl(new mapboxgl.NavigationControl());

const markers = {};
const sockets = {};
const errorDevices = new Set();
let currentIndex = 0;
let trackingData = [];

function showToast(identifier) {
    const toastContainer = document.getElementById("toast-container");

    const toast = document.createElement("div");
    toast.id = `toast-${identifier}`;
    toast.className = "flex items-center w-full max-w-xs p-4 mb-4 text-gray-500 bg-white rounded-lg shadow-sm dark:text-gray-400 dark:bg-gray-800 transition-opacity duration-300 opacity-0";
    toast.setAttribute("role", "alert");

    toast.innerHTML = `
        <div class="inline-flex items-center justify-center shrink-0 w-8 h-8 text-red-500 bg-red-100 rounded-lg dark:bg-red-800 dark:text-red-200">
            <svg class="w-5 h-5" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="currentColor" viewBox="0 0 20 20">
                <path d="M10 .5a9.5 9.5 0 1 0 9.5 9.5A9.51 9.51 0 0 0 10 .5Zm3.707 11.793a1 1 0 1 1-1.414 1.414L10 11.414l-2.293 2.293a1 1 0 0 1-1.414-1.414L8.586 10 6.293 7.707a1 1 0 0 1 1.414-1.414L10 8.586l2.293-2.293a1 1 0 0 1 1.414 1.414L11.414 10l2.293 2.293Z"/>
            </svg>
            <span class="sr-only">Error icon</span>
        </div>
        <div class="ms-3 text-sm font-normal">${identifier} is offline.</div>
        <button type="button" class="ms-auto -mx-1.5 -my-1.5 bg-white text-gray-400 hover:text-gray-900 rounded-lg focus:ring-2 focus:ring-gray-300 p-1.5 hover:bg-gray-100 inline-flex items-center justify-center h-8 w-8 dark:text-gray-500 dark:hover:text-white dark:bg-gray-800 dark:hover:bg-gray-700"
            onclick="this.parentElement.remove()" aria-label="Close">
            <span class="sr-only">Close</span>
            <svg class="w-3 h-3" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 14 14">
                <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="m1 1 6 6m0 0 6 6M7 7l6-6M7 7l-6 6"/>
            </svg>
        </button>
    `;

    toastContainer.appendChild(toast);
    setTimeout(() => {
        toast.classList.remove("opacity-0");
        toast.classList.add("opacity-100");
    }, 50);

    setTimeout(() => {
        toast.classList.add("opacity-0");
        setTimeout(() => toast.remove(), 300);
    }, 5000);
}

async function fetchTrackingUnits() {
    try {
        const response = await fetch('http://localhost:8080/tracking/json');
        trackingData = await response.json(); // Store tracking data globally

        trackingData.forEach(unit => {
            const { gpsIMEI, serverUrl, identifier, latitude, longitude, position } = unit;
            const [lat, lng] = position || [latitude, longitude];

            if (!markers[gpsIMEI]) {
                markers[gpsIMEI] = new mapboxgl.Marker()
                    .setLngLat([lng, lat])
                    .setPopup(new mapboxgl.Popup().setText(`ID: ${identifier}`))
                    .addTo(map);
            }

            if (serverUrl && !sockets[gpsIMEI]) {
                const socket = new WebSocket(serverUrl);

                socket.onopen = () => console.log(`Connected to ${serverUrl}`);

                socket.onmessage = (event) => {
                    try {
                        const data = JSON.parse(event.data);
                        const [newLat, newLng] = data.position || [latitude, longitude];

                        if (markers[gpsIMEI]) {
                            markers[gpsIMEI].setLngLat([newLng, newLat]);
                        }
                    } catch (err) {
                        console.error(`Error processing WebSocket data for ${gpsIMEI}:`, err);
                    }
                };

                socket.onerror = (error) => {
                    console.error(`WebSocket error for ${serverUrl}:`, error);

                    if (!errorDevices.has(gpsIMEI)) {
                        errorDevices.add(gpsIMEI);
                        showToast(identifier);
                    }
                };

                socket.onclose = () => console.log(`WebSocket closed for ${serverUrl}`);

                sockets[gpsIMEI] = socket;
            }
        });

    } catch (error) {
        console.error("Error fetching tracking data:", error);
    }
}

async function updateTrackingData() {
    if (trackingData.length === 0) return;

    const unit = trackingData[currentIndex];

    if (!unit) {
        currentIndex = 0;
        return;
    }

    const { gpsIMEI, identifier, latitude, longitude } = unit;

    // Generate new position (simulate movement)
    const newLat = latitude + (Math.random() * 0.0005 - 0.00025);
    const newLng = longitude + (Math.random() * 0.0005 - 0.00025);

    // Calculate movement difference
    const latDiff = Math.abs(newLat - latitude);
    const lngDiff = Math.abs(newLng - longitude);
    const hasMoved = latDiff > 0.000001 || lngDiff > 0.000001; // More sensitive

    // Determine status based on movement and connection
    let status;
    if (hasMoved) {
        status = "MOVING";
    } else if (sockets[gpsIMEI] && sockets[gpsIMEI].readyState === WebSocket.OPEN) {
        status = "IDLE";
    } else {
        status = "PARKED";
    }

    console.log(`Device: ${gpsIMEI}, Movement: ${hasMoved}, Status: ${status}, LatDiff: ${latDiff}, LngDiff: ${lngDiff}`);

    const requestData = {
        gpsIMEI,
        identifier,
        latitude: newLat,
        longitude: newLng,
        status,
        timestamp: new Date().toISOString()
    };

    try {
        const response = await fetch('http://localhost:8080/t/tracking/update', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(requestData)
        });

        const responseData = await response.json();
        console.log(`Updated ${gpsIMEI}:`, responseData);

        // Ensure marker updates correctly
        if (markers[gpsIMEI]) {
            markers[gpsIMEI].setLngLat([newLng, newLat]);
        }
    } catch (error) {
        console.error(`Error updating tracking data for ${gpsIMEI}:`, error);
    }

    currentIndex = (currentIndex + 1) % trackingData.length;
}



fetchTrackingUnits();
setInterval(updateTrackingData, 30000); // Updates every 30 secs
