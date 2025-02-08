document.addEventListener("DOMContentLoaded", function () {
    fetchData();

    // Tab navigation logic
    document.getElementById("unitsTab").addEventListener("click", () => switchTab("units"));
    document.getElementById("detailsTab").addEventListener("click", () => switchTab("details"));
    document.getElementById("moreTab").addEventListener("click", () => switchTab("more"));
});

function fetchData() {
    fetch("http://localhost:8080/t/tracking/json")
        .then(response => response.json())
        .then(data => populateUnits(data))
        .catch(error => console.error("Error fetching data:", error));
}

function populateUnits(data) {
    const container = document.getElementById("unitsContainer");
    container.innerHTML = "";

    data.forEach(item => {
        const unitDiv = document.createElement("div");
        unitDiv.className = "flex h-20 w-full flex-col rounded-lg bg-white px-2 shadow cursor-pointer p-3";
        unitDiv.innerHTML = `
            <div class="text-lg font-bold">${item.identifier}</div>
            <div class="text-gray-600">IMEI: ${item.gpsIMEI}</div>
            <div class="text-green-600">${item.status}</div>
        `;

        unitDiv.addEventListener("click", () => showDetails(item));
        container.appendChild(unitDiv);
    });
}

function showDetails(item) {
    switchTab("details");
    document.getElementById("detailsContent").innerHTML = `
        <h3 class="text-lg font-semibold">${item.identifier}</h3>
        <p><strong>IMEI:</strong> ${item.gpsIMEI}</p>
        <p><strong>Status:</strong> ${item.status}</p>
        <p><strong>License Number:</strong> ${item.licenseNumber || "N/A"}</p>
        <p><strong>Latitude:</strong> ${item.latitude}</p>
        <p><strong>Longitude:</strong> ${item.longitude}</p>
        <p><strong>Server URL:</strong> ${item.serverUrl}</p>
        <p><strong>Timestamp:</strong> ${item.timestamp}</p>
    `;
}

function switchTab(tab) {
    // Hide all sections
    document.getElementById("unitsContainer").classList.add("hidden");
    document.getElementById("detailsSection").classList.add("hidden");
    document.getElementById("moreSection").classList.add("hidden");

    // Show selected section
    if (tab === "units") document.getElementById("unitsContainer").classList.remove("hidden");
    if (tab === "details") document.getElementById("detailsSection").classList.remove("hidden");
    if (tab === "more") document.getElementById("moreSection").classList.remove("hidden");

    // Update active tab
    document.querySelectorAll(".tab-button").forEach(btn => btn.classList.remove("active-tab"));
    document.getElementById(`${tab}Tab`).classList.add("active-tab");
}

function goBack() {
    switchTab("units");
}