document.addEventListener("DOMContentLoaded", function () {
    const sidebar = document.getElementById("sidebar-multi-level-sidebar");
    const toggleButton = document.querySelector("[data-drawer-toggle='sidebar-multi-level-sidebar']");
    const closeButton = sidebar.querySelector("button"); // Selects the close button inside the sidebar

    function toggleSidebar(event) {
        event.stopPropagation(); // Prevents the outside click event from closing it immediately
        sidebar.classList.toggle("-translate-x-full");
    }

    if (toggleButton) {
        toggleButton.addEventListener("click", toggleSidebar);
    }

    if (closeButton) {
        closeButton.addEventListener("click", toggleSidebar);
    }

    // Close when clicking outside the sidebar (for small screens only)
    document.addEventListener("click", function (event) {
        if (!sidebar.contains(event.target) && !toggleButton.contains(event.target) && window.innerWidth < 640) {
            sidebar.classList.add("-translate-x-full");
        }
    });

    // Ensure sidebar remains open on `md` and `lg` screens
    window.addEventListener("resize", function () {
        if (window.innerWidth >= 768) {
            sidebar.classList.remove("-translate-x-full");
        }
    });
});



// Toggle the dropdown visibility
function toggleDropdown() {
    const dropdown = document.getElementById('profileDropdown');
    dropdown.classList.toggle('hidden');
}

// Close dropdown if clicked outside
window.addEventListener('click', function (e) {
    const dropdown = document.getElementById('profileDropdown');
    const button = document.getElementById('profileButton');
    if (!button.contains(e.target) && !dropdown.contains(e.target)) {
        dropdown.classList.add('hidden');
    }
});


