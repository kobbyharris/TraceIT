function toggleDropdown(button) {
    const dropdown = button.nextElementSibling;
    dropdown.classList.toggle('active');
}

function showVehicleDetails(name, plate, brand, model, type, status) {
    document.getElementById('vehicleDetails').classList.remove('hidden');
    document.getElementById('vehicleName').textContent = name;
    document.getElementById('plateNumber').textContent = plate;
    document.getElementById('brand').textContent = brand;
    document.getElementById('model').textContent = model;
    document.getElementById('type').textContent = type;
    document.getElementById('status').textContent = status;
}

function closeVehicleDetails() {
    document.getElementById('vehicleDetails').classList.add('hidden');
}

// Close dropdowns when clicking outside
document.addEventListener('click', (event) => {
    document.querySelectorAll('[data-dropdown]').forEach(dropdown => {
        dropdown.classList.remove('active');
    });
});

// Close dropdowns when clicking outside
document.addEventListener('click', (event) => {
    document.querySelectorAll('[data-dropdown]').forEach(dropdown => {
        dropdown.classList.remove('active');
    });
});

// Ensure dropdown stays open when clicked
function toggleDropdown(button) {
    const dropdown = button.nextElementSibling;
    document.querySelectorAll('[data-dropdown]').forEach(d => {
        if (d !== dropdown) d.classList.remove('active');
    });
    dropdown.classList.toggle('active');
}

//filtering vehicles dynamically when db is working

function filterVehicles() {
    const searchValue = document.getElementById('vehicleSearch').value.toLowerCase();
    const vehicleCards = document.querySelectorAll('.vehicle-card');

    vehicleCards.forEach(card => {
        const vehicleName = card.querySelector('.vehicle-name').textContent.toLowerCase();
        const plateNumber = card.querySelector('.vehicle-plate').textContent.toLowerCase();

        if (vehicleName.includes(searchValue) || plateNumber.includes(searchValue)) {
            card.classList.remove('hidden');
        } else {
            card.classList.add('hidden');
        }
    });
}
