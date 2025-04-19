const doctorId = localStorage.getItem("doctor");

async function loadAppointments() {
  try {
    const response = await fetch(
      `http://localhost:8080/api/appoint/appointmentsbydoctor?doctorId=${doctorId}`
    );
    const appointments = await response.json();
    const tableBody = document.querySelector("#appointmentTable tbody");
    tableBody.innerHTML = "";

    appointments.forEach((appt) => {
      const row = document.createElement("tr");
      row.innerHTML = `
        <td>${appt.appointmentId}</td>
        <td>${appt.patientId}</td>
        <td>${appt.appointmentDate}</td>
        <td>
          <input type="text" class="form-control" id="prescription-${
            appt.appointmentId
          }" value="${appt.prescription || ""}" />
          <button class="btn btn-sm btn-primary mt-1" onclick="updatePrescription(${
            appt.appointmentId
          })" style="background-color:black">Save</button>
           <button class="btn btn-sm btn-secondary" onclick='printAppointment(${JSON.stringify(
             appt
           )})'>Print</button>
        </td>
      `;
      tableBody.appendChild(row);
    });
  } catch (error) {
    console.error("Failed to fetch appointments", error);
  }
}

async function updatePrescription(appointmentId) {
  const input = document.getElementById(`prescription-${appointmentId}`);
  const prescription = input.value;

  try {
    const response = await fetch(
      "http://localhost:8080/api/appoint/updatePrescription",
      {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          appointmentId: appointmentId,
          prescription: prescription,
        }),
      }
    );

    const result = await response.json();
    alert(result.message || "Prescription updated!");
    loadAppointments(); // Refresh the table
  } catch (error) {
    console.error("Failed to update prescription", error);
    alert("Error updating prescription.");
  }
}

function printAppointment(appt) {
  const printWindow = window.open("", "_blank");
  const prescriptionValue = document.getElementById(
    `prescription-${appt.appointmentId}`
  ).value;

  printWindow.document.write(`
      <html>
        <head>
          <title>Appointment Details</title>
          <style>
            body { font-family: Arial, sans-serif; padding: 20px; }
            h2 { margin-bottom: 20px; }
            table { border-collapse: collapse; width: 100%; }
            td, th { border: 1px solid #ddd; padding: 8px; }
            th { background-color: #f2f2f2; }
          </style>
        </head>
        <body>
          <h2>Appointment Details</h2>
          <table>
            <tr><th>Appointment ID</th><td>${appt.appointmentId}</td></tr>
            <tr><th>Patient Name</th><td>${appt.patientId}</td></tr>
            <tr><th>Date</th><td>${appt.appointmentDate}</td></tr>
            <tr><th>Prescription</th><td>${prescriptionValue}</td></tr>
          </table>
          <script>
            window.onload = function() {
              window.print();
            };
          </script>
        </body>
      </html>
    `);

  printWindow.document.close();
}

// Load appointments on page load
window.onload = loadAppointments;
