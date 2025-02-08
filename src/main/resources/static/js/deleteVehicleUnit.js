function deleteVehicle(vehicleId) {
    const url = `/t/vehicles/d/${vehicleId}`;

    // Make an AJAX DELETE request
    fetch(url, {
      method: 'DELETE',
    })
    .then(response => {
      if (response.ok) {
        // Redirect to the vehicles page after successful deletion
        window.location.href = "/t/vehicles";
      } else {
        alert("Failed to delete the vehicle.");
      }
    })
    .catch(error => {
      console.error("Error deleting vehicle:", error);
      alert("Error deleting the vehicle.");
    });
 }