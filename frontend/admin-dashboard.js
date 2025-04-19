const btnAddDoctor = document.getElementById("btn-add-doctor");
const btnCreateAppointment = document.getElementById("btn-create-appointment");
const addDoctorForm = document.getElementById("add-doctor-form");
const loginForm = document.getElementById("btn-add-login");
const createAppointmentForm = document.getElementById(
  "create-appointment-form"
);

btnAddDoctor.addEventListener("click", () => {
  addDoctorForm.classList.add("active");
  createAppointmentForm.classList.remove("active");
});

btnCreateAppointment.addEventListener("click", () => {
  createAppointmentForm.classList.add("active");
  addDoctorForm.classList.remove("active");
  loadDoctors();
});

loginForm.addEventListener("click", () => {
  window.location.href = "index.html";
});

// Doctor form submission
document.getElementById("doctorForm").addEventListener("submit", async (e) => {
  e.preventDefault();

  const doctor = {
    doctorId: document.getElementById("doctorId").value,
    doctorName: document.getElementById("doctorName").value,
    password: document.getElementById("password").value,
  };

  try {
    const response = await fetch("http://localhost:8080/api/doctor/create", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(doctor),
    });

    const result = await response.text();
    alert(result);

    if (result.includes("success")) {
      document.getElementById("doctorForm").reset();
    }
  } catch (error) {
    console.error("Error creating doctor:", error);
    alert("Error occurred while creating doctor.");
  }
});

// Appointment form submission
document
  .getElementById("appointmentForm")
  .addEventListener("submit", async (e) => {
    e.preventDefault();

    const appointment = {
      patientId: document.getElementById("patientName").value,
      doctorId: document.getElementById("doctorIdSelect").value,
      appointmentDate: document.getElementById("appointmentDate").value,
    };

    try {
      const response = await fetch(
        "http://localhost:8080/api/doctor/bookAppointment",
        {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(appointment),
        }
      );

      const result = await response.json();
      alert(result.message || "Appointment created successfully!");
      document.getElementById("appointmentForm").reset();
      loadAppointments();
    } catch (err) {
      console.error(err);
      alert("Error creating appointment.");
    }
  });

// Fetch and populate doctor dropdown
async function loadDoctors() {
  try {
    const response = await fetch(
      "http://localhost:8080/api/doctor/getdoctordata"
    );
    const doctors = await response.json();

    const select = document.getElementById("doctorIdSelect");
    select.innerHTML = "";
    doctors.forEach((doc) => {
      const option = document.createElement("option");
      option.value = doc.doctorId;
      option.textContent = `${doc.doctorName} (${doc.doctorId})`;
      select.appendChild(option);
    });
  } catch (err) {
    console.error("Failed to load doctors:", err);
    alert("Unable to load doctor list.");
  }
}

// Load doctors when page is ready

async function loadAppointments() {
  try {
    const response = await fetch(
      "http://localhost:8080/api/doctor/getappointmentdata"
    );
    const appointments = await response.json();

    const tableBody = document.querySelector("#appointmentTable tbody");
    tableBody.innerHTML = ""; // Clear before appending

    appointments.forEach((appt) => {
      const row = document.createElement("tr");
      row.innerHTML = `
        <td>${appt.appointmentId}</td>
        <td>${appt.patientId}</td>
        <td>${appt.doctorId}</td>
        <td>${appt.appointmentDate}</td>
        <td>${appt.prescription || "-"}</td>
      `;
      tableBody.appendChild(row);
    });
  } catch (err) {
    console.error("Failed to load appointments:", err);
  }
}

// Call it after page load or after form submission
window.onload = loadAppointments;
document.addEventListener("DOMContentLoaded", loadDoctors);
