document.addEventListener("DOMContentLoaded", function() {
        // Check if location permission was already granted
        if (localStorage.getItem("locationAllowed") === "true") {
            navigator.geolocation.getCurrentPosition(
                (position) => {
                    saveLocation(position);
                },
                (error) => {
                    console.error("Error getting location:", error.message);
                }
            );
        } else {
            // Show the toast only if permission has not been granted
            setTimeout(() => {
                document.getElementById("location-toast").classList.remove("hidden");
            }, 1000);
        }
    });

    function closeToast() {
        document.getElementById("location-toast").classList.add("hidden");
    }

    function requestLocationPermission() {
        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(
                (position) => {
                    saveLocation(position);
                    localStorage.setItem("locationAllowed", "true"); // Save permission status
                    closeToast(); // Hide toast after allowing
                },
                (error) => {
                    console.error("Error getting location:", error.message);
                }
            );
        } else {
            alert("Geolocation is not supported by this browser.");
        }
    }

    function saveLocation(position) {
        // Select all longitude and latitude inputs across both forms
        document.querySelectorAll("#longitude").forEach(input => input.value = position.coords.longitude);
        document.querySelectorAll("#latitude").forEach(input => input.value = position.coords.latitude);

        // Save to local storage
        localStorage.setItem("longitude", position.coords.longitude);
        localStorage.setItem("latitude", position.coords.latitude);
    }

    // Restore saved location values if available
    document.addEventListener("DOMContentLoaded", function() {
        if (localStorage.getItem("longitude") && localStorage.getItem("latitude")) {
            document.querySelectorAll("#longitude").forEach(input => input.value = localStorage.getItem("longitude"));
            document.querySelectorAll("#latitude").forEach(input => input.value = localStorage.getItem("latitude"));
        }
    });