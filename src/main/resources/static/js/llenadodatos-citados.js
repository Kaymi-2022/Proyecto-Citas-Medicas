var editUserModal = document.getElementById("editUserModal");
editUserModal.addEventListener("show.bs.modal", function (event) {
    var button = event.relatedTarget; // Botón que abrió el modal
    var id = button.getAttribute("data-id");
    var fecha = button.getAttribute("data-fecha");
    var paciente = button.getAttribute("data-paciente");
    var consultorio = button.getAttribute("data-consultorio");
    var medico = button.getAttribute("data-medico");
    var actividad = button.getAttribute("data-actividad");
    var hora = button.getAttribute("data-hora");
    var situacion = button.getAttribute("data-situacion");

    var idInput = editUserModal.querySelector("#id");
    var fechaInput = editUserModal.querySelector("#fecha");
    var pacienteInput = editUserModal.querySelector("#paciente");
    var consultorioInput = editUserModal.querySelector("#consultorio");
    var medicoInput = editUserModal.querySelector("#medico");
    var actividadInput = editUserModal.querySelector("#actividad");
    var horaInput = editUserModal.querySelector("#hora");
    var situacionInput = editUserModal.querySelector("#situacion");

    idInput.value = id;
    fechaInput.value = fecha;
    pacienteInput.value = paciente;
    consultorioInput.value = consultorio;
    medicoInput.value = medico;
    actividadInput.value = actividad;
    horaInput.value = hora;
    situacionInput.value = situacion;
});