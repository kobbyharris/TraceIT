
// Fetch driver data from backend
async function fetchDriverData() {
    try {
        const response = await fetch('http://localhost:8080/t/drivers/json'); // Adjust URL as needed
        const drivers = await response.json();
        console.log(drivers); // Log the data to ensure it's coming through
        populateDriverGrid(drivers);
    } catch (error) {
        console.error("Error fetching drivers:", error);
    }
}

// Populate the driver grid with data from the backend
function populateDriverGrid(drivers) {
    const driverList = document.getElementById('driverList');
    driverList.innerHTML = ''; // Clear existing content

    drivers.forEach(driver => {
        const card = document.createElement('div');
        card.classList.add('driver-card');
        card.setAttribute('data-name', driver.firstName + ' ' + driver.lastName);

        card.innerHTML = `
            <div class="bg-gray-50 p-4 rounded-lg shadow-sm border relative group cursor-pointer" 
                onclick="showDriverDetails('${driver.driverId}', '${driver.firstName}', '${driver.lastName}', '${driver.contact}', '${driver.vehicle.vehicleId}', '${driver.portraitUrl}')">
                <img src="${driver.portraitUrl}" alt="Driver Image" class="mb-2 rounded-full w-24 h-24 object-cover">
                <p class="font-semibold">${driver.firstName} ${driver.lastName}</p>
                <p class="text-sm text-gray-500">Contact: ${driver.contact}</p>
            </div>
        `;

        driverList.appendChild(card);
    });
}

// Show driver details in the modal
function showDriverDetails(driverId, firstName, lastName, contact, vehicleId, portraitUrl) {
    document.getElementById('driverName').textContent = firstName + ' ' + lastName;
    document.getElementById('telephone').textContent = contact;
    document.getElementById('vehicle').textContent = vehicleId; // You may want to fetch the vehicle details separately
    document.getElementById('licenseNumber').textContent = 'N/A'; // Placeholder or change as needed
    document.getElementById('driverStatus').textContent = 'Active'; // Change status logic as needed
    document.getElementById('driverImage').src = portraitUrl;

    document.getElementById('editLink').href = `/t/driver-edit-unit/${driverId}`;
    document.getElementById('deleteLink').href = `/t/driver-delete-unit/${driverId}`;
    document.getElementById('viewVehicleLink').href = `/t/vehicle/${vehicleId}`;

    document.getElementById('driverModal').classList.remove('hidden');
}

// Close the driver details modal
function closeDriverDetails() {
    document.getElementById('driverModal').classList.add('hidden');
}

// Filter the drivers based on the search input
function filterDrivers() {
    const searchTerm = document.getElementById('driverSearch').value.toLowerCase();
    const driverCards = document.querySelectorAll('.driver-card');

    driverCards.forEach(card => {
        const driverName = card.getAttribute('data-name').toLowerCase();

        if (driverName.includes(searchTerm)) {
            card.style.display = '';
        } else {
            card.style.display = 'none';
        }
    });
}

// Fetch data when the page loads
window.onload = fetchDriverData;
