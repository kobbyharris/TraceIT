
            // Filter the vehicles based on the search input
            function filterVehicles() {
                const searchTerm = document.getElementById('vehicleSearch').value.toLowerCase();
                const vehicleCards = document.querySelectorAll('.vehicle-card');

                vehicleCards.forEach(card => {
                    const licensePlate = card.getAttribute('data-plate').toLowerCase();

                    if (licensePlate.includes(searchTerm)) {
                        card.style.display = '';
                    } else {
                        card.style.display = 'none';
                    }
                });
            }

            // Fetch data when the page loads
            window.onload = fetchVehicleData;