document.addEventListener("DOMContentLoaded", function() {

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

        setTimeout(() => {
            document.getElementById("location-toast").classList.remove("hidden");
        }, 1000);
    }

    // Restore saved location values if available
    if (localStorage.getItem("longitude") && localStorage.getItem("latitude")) {
        document.getElementById("longitude").value = localStorage.getItem("longitude");
        document.getElementById("latitude").value = localStorage.getItem("latitude");
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
    document.getElementById("longitude").value = position.coords.longitude;
    document.getElementById("latitude").value = position.coords.latitude;
    localStorage.setItem("longitude", position.coords.longitude);
    localStorage.setItem("latitude", position.coords.latitude);
}

document.getElementById('openForm').addEventListener('click', function () {
    document.getElementById('modal').classList.remove('hidden');
});
document.getElementById('closeForm').addEventListener('click', function () {
    document.getElementById('modal').classList.add('hidden');
});
