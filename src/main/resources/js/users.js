function loadUsers() {
    fetch('/admin/users/list')
        .then(response => response.json())
        .then(data => {
            const tableBody = document.querySelector('#userTable tbody');
            tableBody.innerHTML = ''; // Очистить таблицу перед добавлением данных

            data.forEach(user => {
                const row = document.createElement('tr');

                row.innerHTML = `
                    <td>${user.name}</td>
                    <td>${user.surname}</td>
                    <td>${user.position}</td>
                    <td>${user.email}</td>
                    <td>
                        <a href="#" onclick="editUserByEmail('${user.email}')">Редактировать</a> |
                        <a href="#" onclick="deleteUserByEmail('${user.email}')">Удалить</a>
                    </td>
                `;

                tableBody.appendChild(row);
            });

            document.getElementById('userTableContainer').style.display = 'block';
        })
        .catch(error => {
            console.error('Ошибка при получении пользователей:', error);
        });
}

// Открывает форму добавления пользователя
function showAddUserForm() {
    document.getElementById('addUserForm').style.display = 'block';
}

// Отменяет добавление пользователя
function cancelAdd() {
    document.getElementById('addUserForm').style.display = 'none';
}

// Добавляет нового пользователя
function addUser() {
    const newUser = {
        name: document.getElementById('addName').value,
        surname: document.getElementById('addSurname').value,
        position: document.getElementById('addPosition').value,
        email: document.getElementById('addEmail').value
    };

    fetch('/admin/users/add', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(newUser)
    })
        .then(response => {
            if (response.ok) {
                alert('Пользователь успешно добавлен.');
                loadUsers();
                cancelAdd();
            } else {
                alert('Ошибка при добавлении пользователя.');
            }
        })
        .catch(error => {
            console.error('Ошибка при добавлении пользователя:', error);
        });
}

// Открывает форму редактирования с данными пользователя
function editUserByEmail(email) {
    fetch(`/admin/users/getByEmail?email=${encodeURIComponent(email)}`)
        .then(response => response.json())
        .then(user => {
            document.getElementById('editName').value = user.name;
            document.getElementById('editSurname').value = user.surname;
            document.getElementById('editPosition').value = user.position;
            document.getElementById('editEmail').value = user.email;

            document.getElementById('editUserForm').style.display = 'block';
        })
        .catch(error => {
            console.error('Ошибка при загрузке данных пользователя:', error);
        });
}

// Сохраняет изменения пользователя
function saveUserChanges() {
    const updatedUser = {
        name: document.getElementById('editName').value,
        surname: document.getElementById('editSurname').value,
        position: document.getElementById('editPosition').value,
        email: document.getElementById('editEmail').value
    };

    fetch('/admin/users/edit', {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(updatedUser)
    })
        .then(response => {
            if (response.ok) {
                alert('Данные пользователя успешно обновлены.');
                loadUsers();
                cancelEdit();
            } else {
                alert('Ошибка при сохранении изменений.');
            }
        })
        .catch(error => {
            console.error('Ошибка при обновлении данных пользователя:', error);
        });
}

// Отменяет редактирование
function cancelEdit() {
    document.getElementById('editUserForm').style.display = 'none';
}

// Удаляет пользователя по email
function deleteUserByEmail(email) {
    if (confirm('Вы уверены, что хотите удалить пользователя?')) {
        fetch(`/admin/users/delete?email=${encodeURIComponent(email)}`, {
            method: 'DELETE'
        })
            .then(response => {
                if (response.ok) {
                    alert('Пользователь удален.');
                    loadUsers();
                } else {
                    alert('Ошибка при удалении пользователя.');
                }
            })
            .catch(error => {
                console.error('Ошибка при удалении пользователя:', error);
            });
    }
}