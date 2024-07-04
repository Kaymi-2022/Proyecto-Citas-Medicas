  var editUserModal = document.getElementById("editUserModal");
  editUserModal.addEventListener("show.bs.modal", function (event) {
  var link = event.relatedTarget; // Enlace que abrió el modal
  var horarioid = link.getAttribute("data-id");
  var dia = link.getAttribute("data-dia");
  var time = link.getAttribute("data-time");
  var idestadoCita = link.getAttribute("data-idestadoCita");
  var idMedico = link.getAttribute("data-idMedico");

  var idHorarioInput = editUserModal.querySelector("#idHorario");
  var diaInput = editUserModal.querySelector("#dia");
  var timeInput = editUserModal.querySelector("#time");
  var idestadoCitaInput = editUserModal.querySelector("#idestadoCita");
  var medicosInput = editUserModal.querySelector("#medicos");

  idHorarioInput.value = horarioid;
  diaInput.value = dia;
  timeInput.value = time;
  idestadoCitaInput.value = idestadoCita;
  medicosInput.value = idMedico;
});
