function toggleSidebar() {
    const sidebar = document.getElementById('sidebar');
    sidebar.classList.toggle('hidden');
};



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


