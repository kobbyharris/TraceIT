document.addEventListener("DOMContentLoaded", function () {
    const modal = document.getElementById("popup-modal");
    const openModalBtn = document.querySelector("[data-modal-toggle='popup-modal']");
    const closeModalBtns = document.querySelectorAll("[data-modal-hide='popup-modal']");


    function showModal() {
        modal.classList.remove("hidden");
        modal.classList.add("flex");
    }

    function hideModal() {
        modal.classList.add("hidden");
        modal.classList.remove("flex");
    }


    openModalBtn.addEventListener("click", showModal);

    closeModalBtns.forEach(button => {
        button.addEventListener("click", hideModal);
    });

    modal.addEventListener("click", function (event) {
        if (event.target === modal) {
            hideModal();
        }
    });
});