$(document).ready(function () {

    allUsers();

    function allUsers() {
        $.ajax({
            url: "rest/admin/",
            method: "GET",
            dataType: "json",
            success: function (data) {

                var tableBody = $('#myTbody');
                tableBody.empty();
                $(data).each(function (i, user) {//перебираем все элементы в data
                    var stringRoles = [];
                    for (var y = 0; y < user.roles.length; y++) {
                        stringRoles.push(user.roles[y].role);
                    }

                    tableBody.append(`<tr id="${user.id}">
                    <td id="userId-${user.id}">${user.id}</td>
                    <td id="userName-${user.id}">${user.name}</td>
                    <td id="userEmail-${user.id}">${user.email}</td>
                    <td id="userPassword-${user.id}">${user.password}</td>
                    <td id="userRoles-${user.id}">${stringRoles}</td>
                    <td><button type="button" class="btn btn-info edit-user" data-toggle="modal" data-target="#windowModal"
                    id="editModalButton-${user.id}">Edit</button></td>
                    <td><button type="button" class="btn btn-danger delete-user" data-toggle="modal" data-target="#windowModal"
                    id="deleteModalButton-${user.id}">Delete</button></td>
                    </tr>`);
                })
            },
            error: function (error) {
                alert(error);
            }
        })
    }

    $('#addButton').click(function (e) {
        e.preventDefault();//отмена действия браузера по умолчанию (действия на сервер) - при клике на кнопку не произойдет отправка Post, она произойдет потом
        $('#addingNewUserDiv').html('<h4>New User added!</h4>').fadeOut(2000, function () {


            var newObject = {};
            newObject ["name"] = $("#nameTextInput").val();
            newObject["password"] = $("#userPasswordTextInput").val();
            newObject["email"] = $("#emailTextInput").val();
            newObject["roles"] = $("#roleSelectNU").val();

            $.ajax({
                url: 'rest/add',
                method: 'post',
                contentType: "application/json;charset=UTF-8",
                data: JSON.stringify(newObject),//отправляем на сервер JSON преобразовав объект newObject через метод JSON.stringify()
                dataType: 'json',
                context: document.getElementById('addingNewUserDiv'),// this = document.getElementById(..)
                success: function (data) {//data - данные с сервера (DTO)
                    $("#addForm").trigger("reset");
                    var tableBody = $('#myTbody');

                    $(data).each(function (i, user) {//перебираем все элементы в data
                        var stringRoles = [];
                        for (var y = 0; y < user.roles.length; y++) {
                            stringRoles.push(user.roles[y].role);
                        }

                    tableBody.append(`<tr id="${data.id}">
                    <td id="userId-${data.id}">${data.id}</td>
                    <td id="userName-${data.id}">${data.name}</td>
                    <td id="userEmail-${data.id}">${data.email}</td>
                    <td id="userEmail-${data.id}">${data.password}</td>
                    <td id="userRoles-${data.id}">${stringRoles}</td>
                    <td><button type="button" class="btn btn-info edit-user" data-toggle="modal" data-target="#windowModal"
                    id="editModalButton-${data.id}">Edit</button></td>
                    <td><button type="button" class="btn btn-danger delete-user" data-toggle="modal" data-target="#windowModal"
                    id="deleteModalButton-${data.id}">Delete</button></td>
                    </tr>`); })
                },
                error:
                    function () {
                        alert("Error in ADD");
                    }
            });
        });
    });

    $('#myTbody').on('click', '.delete-user', function () {
        document.getElementById('ModalTitle').textContent = 'Delete User';
        document.getElementById('actionButton').textContent = 'Delete User';
        document.getElementById('actionButton').className = 'btn btn-danger';
        $('.inputs').attr('disabled', '');
        $('.passwordInput').remove();
        showModalValuesWindow(this);
    });

    $('.modal-footer').on('click', 'a.btn-danger', function () {
        var id = $("#IdInput").val();
        var delObj = $('#deleteModalButton-' + id);
        $.ajax({
            url: '/rest/delete/' + id,
            method: 'delete',
            contentType: "application/json;charset=UTF-8",

            success: function () {
                $(delObj).closest('tr').css('background', 'lightcoral');
                $(delObj).closest('tr').fadeOut(2000, function () {
                    $(delObj).remove();
                });
                $('#closeButton').click();
            },
            error: function () {
                alert("Error Delete bleat!");
            }
        });
        $('#windowModal .closeSecondButton').click();
    });


    $('#myTbody').on('click', '.edit-user', function () {
        var currentObject = this;
        document.getElementById('ModalTitle').textContent = 'Edit User';
        document.getElementById('actionButton').textContent = 'Edit User';
        document.getElementById('actionButton').className = 'btn btn-info';
        $('.inputs').removeAttr('disabled');
        showModalValuesWindow(currentObject);
    });

    //делегированная обработка. Делаю модальное окно изменяю имя класса "actionButton", чтобы дальше обратиться к нему
    // надо сделать уточнитель-селектор в методе on
    //  Выборка - в классе .modal-footer для любого нового элемента a с классом btn-info будем делать следующее
    $('.modal-footer').on('click', 'a.btn-info', function () {
        var updateObject = {};
        updateObject["id"] = $("#IdInput").val();
        updateObject["name"] = $("#nameInput").val();
        updateObject["email"] = $("#emailInput").val();
        updateObject["password"] = $("#passwordInput").val();
        updateObject["roles"] = $("#roleSelectInput").val();

        $.ajax({
            url: '/rest/edit',
            method: "put",
            contentType: "application/json;charset=UTF-8",
            data: JSON.stringify(updateObject),
            dataType: 'json',
            success: function (data) {

                $(data).each(function (i, user) {//перебираем все элементы в data
                        var stringRoles = [];
                        for (var y = 0; y < user.roles.length; y++) {
                            stringRoles.push(user.roles[y].role);
                        }

                var id = data.id;
                var name = data.name;
                var email = data.email;
                var roles = stringRoles;
                $('#userId-' + id).text(id);
                $('#userName-' + id).text(name);
                $('#userEmail-' + id).text(email);
                $('#userRoles-' + id).text(roles);
                $('#windowModal .closeSecondButton').click();})
            },
            error: function () {
                alert("Error!");
            }
        })
    });


    function showModalValuesWindow(object) {
        var id = object.id.slice(object.id.lastIndexOf("-") + 1);
        $('#IdInput').attr('value', id);
        $('#nameInput').attr('value', $('#userName-' + id).text());
        $('#passwordInput').attr('value');
        $('#positionInput').attr('value', $('#userPosition-' + id).text());
        $('#ageInput').attr('value', $('#userAge-' + id).text());
        $('#emailInput').attr('value', $('#userEmail-' + id).text());

        var userRow = $("[id=" + id + "]");
        var rolesList = ["ADMIN", "USER"];
        var userRoles = userRow.find('#userRoles-' + id).text();
        $('#roleSelectInput').empty();
        rolesList.forEach(function (value) {
            if (userRoles.includes(value)) {
                $('#roleSelectInput').append('<option id="option"' + value + ' value="' + value + '" selected>' + value + '</option>')
            } else {
                $('#roleSelectInput').append('<option id="option"' + value + ' value="' + value + '">' + value + '</option>')
            }
        });
    }
});

