
$('#myModal').on('show.bs.modal', function (event) {
    const button = $(event.relatedTarget);
    const idEdit = button.data('id');
    const emailEdit = button.data('email');
    const nameEdit = button.data('name');
    const modal = $(this);

    modal.find('#idEdit').val(idEdit);
    modal.find('#emailEdit').val(emailEdit);
    modal.find('#nameEdit').val(nameEdit);

})

$('#myModalDelete').on('show.bs.modal', function (event) {
    const button = $(event.relatedTarget);
    const idDelete = button.data('id');
    const emailDelete = button.data('email');
    const nameDelete = button.data('name');
    const passwordDelete = button.data('password');
    const roleDelete = button.data('role');
    const modal = $(this);


    modal.find('#passwordDelete').val(passwordDelete);
    modal.find('#idDelete').val(idDelete);
    modal.find('#emailDelete').val(emailDelete);
    modal.find('#nameDelete').val(nameDelete);
    modal.find('#roleDelete').val(roleDelete);
})