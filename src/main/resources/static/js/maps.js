let map;
let vehicleMarker;

// Initialize Google Map
function initMap() {
    const mapOptions = {
        center: { lat: 5.614818, lng: -0.205874 }, // Replace with your default coordinates
        zoom: 12,
    };

    // Create the map
    map = new google.maps.Map(document.getElementById("map"), mapOptions);

    // Add vehicle marker
    vehicleMarker = new google.maps.Marker({
        position: { lat: 5.614818, lng: -0.205874 }, // Default position
        map: map,
        title: "Vehicle Location",
        icon: {
            url: "https://maps.google.com/mapfiles/kml/shapes/cabs.png", // Replace with your icon
            scaledSize: new google.maps.Size(40, 40), // Resize the icon
        },
    });
}

// Simulate real-time updates from the backend
function fetchVehicleData() {
    // Replace with your API endpoint
    fetch("/api/vehicle/location")
        .then((response) => response.json())
        .then((data) => {
            const { latitude, longitude, address, status, distance, time } = data;

            // Update marker position
            const newLatLng = new google.maps.LatLng(latitude, longitude);
            vehicleMarker.setPosition(newLatLng);

            // Update floating box content
            document.getElementById("vehicle-address").innerText = address;
            document.getElementById("vehicle-location").innerText = `${latitude}, ${longitude}`;
            document.getElementById("vehicle-status").innerText = status;
            document.getElementById("vehicle-distance").innerText = `${distance} km`;
            document.getElementById("vehicle-time").innerText = time;

            // Update map center
            map.setCenter(newLatLng);
        })
        .catch((error) => console.error("Error fetching vehicle data:", error));
}

// Initialize map and set periodic data updates
document.addEventListener("DOMContentLoaded", () => {
    initMap();

    // Fetch vehicle data every 5 seconds
    setInterval(fetchVehicleData, 5000);
});
