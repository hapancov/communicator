'use strict';
const groupPage = document.querySelector('#createGroupForm');
const usernamePage = document.querySelector('#username-page');
const chatPage = document.querySelector('#chat-page');
const usernameForm = document.querySelector('#usernameForm');
const messageForm = document.querySelector('#messageForm');
const messageInput = document.querySelector('#message');
const connectingElement = document.querySelector('.connecting');
const chatArea = document.querySelector('#chat-messages');
const logout = document.querySelector('#logout');
const usersSelect = document.querySelector('#users_group_list');
let stompClient = null;
let nickname = null;
let fullname = null;
let userID = null;
let selectedUserId = null;
let selectedGroupId = null;
let isContactsListVisible = true;
let clickedGroup = null;
document.getElementById('createGroupBtn').addEventListener('click', showCreateGroupForm);
document.getElementById('backToChatContainer').addEventListener('click', backToChat);

function backToChat(event) {
    event.preventDefault();
    document.getElementById('chat-page').classList.remove('hidden');
    document.getElementById('create-group-page').classList.add('hidden');
}


//створення групи

function showCreateGroupForm() {
    document.getElementById('chat-page').classList.add('hidden');
    document.getElementById('create-group-page').classList.remove('hidden');
    findAndDisplayConnectedUsersForGroup();

}

async function findAndDisplayConnectedUsersForGroup() {
    const connectedUsersResponse = await fetch('/users');
    let connectedUsers = await connectedUsersResponse.json();
    connectedUsers = connectedUsers.filter(user => user.nickName !== nickname);
    const connectedUsersList = document.getElementById('users_group_list');
    connectedUsersList.innerHTML = '';

    connectedUsers.forEach(user => {
        appendUserElementForGroup(user, connectedUsersList);
    });
}

function appendUserElementForGroup(user, connectedUsersList) {
    const listItem = document.createElement('option');
    listItem.classList.add('user-item');
    listItem.id = user.nickName;
    const usernameSpan = document.createElement('span');
    usernameSpan.textContent = user.fullName;

    listItem.appendChild(usernameSpan);
    connectedUsersList.appendChild(listItem);
}

function selectGroupInfo(event) {
    name = document.querySelector('#groupName').value.trim();
    let users = Array.from(usersSelect.selectedOptions).map(option => option.id)
    users.push(nickname);
    if (name && users.length > 0) {

        stompClient.send("/app/group.createGroup",
            {},
            JSON.stringify({name: name, users: users})
        );
        document.getElementById('chat-page').classList.remove('hidden');
        document.getElementById('create-group-page').classList.add('hidden');
    }
    setTimeout(findAndDisplayConnectedGroup, 1000)
    event.preventDefault();
}

//групи
async function findAndDisplayConnectedGroup() {

    const connectedGroupResponse = await fetch('/group');
    let connectedGroup = await connectedGroupResponse.json();
    connectedGroup = connectedGroup.filter(group => {
        return group.usersId.includes(userID.toString());
    });

    const connectedGroupList = document.getElementById('connectedGroup');
    connectedGroupList.innerHTML = '';

    connectedGroup.forEach(group => {
        appendGroupElement(group, connectedGroupList);
        if (connectedGroup.indexOf(group) < connectedGroup.length - 1) {
            const separator = document.createElement('li');
            separator.classList.add('separator');
            connectedGroupList.appendChild(separator);
        }
    });
}

function appendGroupElement(group, connectedUsersList) {


    const listItem = document.createElement('li');
    listItem.classList.add('user-item');
    listItem.id = group.name;


    const userImage = document.createElement('img');
    userImage.src = '../img/group_icon.png';
    userImage.alt = group.fullName;

    const usernameSpan = document.createElement('span');
    usernameSpan.textContent = group.name;

    listItem.appendChild(userImage);
    listItem.appendChild(usernameSpan);

    listItem.addEventListener('click', groupItemClick);

    connectedUsersList.appendChild(listItem);
}

// переписка в групах

function groupItemClick(event) {
    document.querySelectorAll('.user-item').forEach(item => {
        item.classList.remove('active');
    });
    messageForm.classList.remove('hidden');

    clickedGroup = event.currentTarget;

    clickedGroup.classList.add('active');

    selectedGroupId = clickedGroup.getAttribute('id');

    fetchAndDisplayGroupChat().then();

    const nbrMsg = clickedGroup.querySelector('.nbr-msg');
    nbrMsg.classList.add('hidden');
    nbrMsg.textContent = '0';
}

async function fetchAndDisplayGroupChat() {
    const userChatResponse = await fetch(`/group/messages/${nickname}/${selectedGroupId}`);
    const userChat = await userChatResponse.json();

    chatArea.innerHTML = '';
    userChat.forEach(chat => {
        displayMessageGroup(chat.senderId, chat.content);
    });
    chatArea.scrollTop = chatArea.scrollHeight;
}


function displayMessageGroup(senderId, content) {
    const messageContainer = document.createElement('div');
    messageContainer.classList.add('message');
    if (senderId === nickname) {
        messageContainer.classList.add('sender');
    } else {
        messageContainer.classList.add('receiver');
        const user = document.createElement('p');
        user.classList.add('username_bold');
        user.textContent = senderId;
        messageContainer.appendChild(user);
    }
    const message = document.createElement('p');
    message.textContent = content;
    messageContainer.appendChild(message);
    chatArea.appendChild(messageContainer);
}


//далі переписка користувачів


function connect(event) {
    nickname = document.querySelector('#nickname').value.trim();
    fullname = document.querySelector('#fullname').value.trim();

    if (nickname && fullname) {
        usernamePage.classList.add('hidden');
        chatPage.classList.remove('hidden');

        const socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);

        stompClient.connect({}, onConnected, onError);
    }
    event.preventDefault();
}


function onConnected() {
    stompClient.subscribe(`/user/${nickname}/queue/messages`, onMessageReceived);
    stompClient.subscribe(`/user/public`, onMessageReceived);
    stompClient.send("/app/user.addUser",
        {},
        JSON.stringify({nickName: nickname, fullName: fullname, status: 'ONLINE'})
    );
    document.querySelector('#connected-user-fullname').textContent = fullname;
    findAndDisplayConnectedUsers().then();
    findAndDisplayConnectedGroup().then();
}

async function findAndDisplayConnectedUsers() {

    const connectedUsersResponse = await fetch('/users');
    let connectedUsers = await connectedUsersResponse.json();
    const user = connectedUsers.find(user => user.nickName === nickname);

    if (user) {
        userID = user.id;
    } else {
        console.log("Користувача з таким нікнеймом не знайдено.");
    }
    connectedUsers = connectedUsers.filter(user => user.nickName !== nickname);
    const connectedUsersList = document.getElementById('connectedUsers');
    connectedUsersList.innerHTML = '';

    connectedUsers.forEach(user => {
        appendUserElement(user, connectedUsersList);
        if (connectedUsers.indexOf(user) < connectedUsers.length - 1) {
            const separator = document.createElement('li');
            separator.classList.add('separator');
            connectedUsersList.appendChild(separator);
        }
    });
}

function appendUserElement(user, connectedUsersList) {
    const listItem = document.createElement('li');
    listItem.classList.add('user-item');
    listItem.id = user.nickName;

    const userImage = document.createElement('img');
    userImage.src = '../img/user_icon.png';
    userImage.alt = user.fullName;

    const usernameSpan = document.createElement('span');
    usernameSpan.textContent = user.fullName;

    const receivedMsgs = document.createElement('span');
    receivedMsgs.textContent = '0';
    receivedMsgs.classList.add('nbr-msg', 'hidden');

    listItem.appendChild(userImage);
    listItem.appendChild(usernameSpan);
    listItem.appendChild(receivedMsgs);

    listItem.addEventListener('click', userItemClick);

    connectedUsersList.appendChild(listItem);
}

function userItemClick(event) {
    document.querySelectorAll('.user-item').forEach(item => {
        item.classList.remove('active');
    });
    messageForm.classList.remove('hidden');

    const clickedUser = event.currentTarget;
    clickedUser.classList.add('active');

    selectedUserId = clickedUser.getAttribute('id');
    fetchAndDisplayUserChat().then();

    const nbrMsg = clickedUser.querySelector('.nbr-msg');
    nbrMsg.classList.add('hidden');
    nbrMsg.textContent = '0';

}

function displayMessage(senderId, content) {
    const messageContainer = document.createElement('div');
    messageContainer.classList.add('message');
    if (senderId === nickname) {
        messageContainer.classList.add('sender');
    } else {
        messageContainer.classList.add('receiver');
    }
    const message = document.createElement('p');
    message.textContent = content;
    messageContainer.appendChild(message);
    chatArea.appendChild(messageContainer);
}

async function fetchAndDisplayUserChat() {
    const userChatResponse = await fetch(`/messages/${nickname}/${selectedUserId}`);
    const userChat = await userChatResponse.json();
    chatArea.innerHTML = '';
    userChat.forEach(chat => {
        displayMessage(chat.senderId, chat.content);
    });
    chatArea.scrollTop = chatArea.scrollHeight;
}


function onError() {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}


function sendMessage(event) {
    const messageContent = messageInput.value.trim();
    if (messageContent && stompClient) {
        if (isContactsListVisible) {
            const chatMessage = {
                senderId: nickname,
                recipientId: selectedUserId,
                content: messageInput.value.trim(),
                timestamp: new Date()
            };
            stompClient.send("/app/chat", {}, JSON.stringify(chatMessage));
            displayMessage(nickname, messageInput.value.trim());
            messageInput.value = '';
        } else {
            const chatMessage = {
                senderId: nickname,
                recipientId: selectedGroupId,
                content: messageInput.value.trim(),
                timestamp: new Date()

            };
            messageInput.value = '';
            stompClient.send("/app/group/chat", {}, JSON.stringify(chatMessage));

        }


    }
    chatArea.scrollTop = chatArea.scrollHeight;
    event.preventDefault();
}


async function onMessageReceived(payload) {
    await findAndDisplayConnectedUsers();
    await findAndDisplayConnectedGroup();

    console.log('Message received', payload);
    const message = JSON.parse(payload.body);
    if (message.recipientId === selectedGroupId) {
        displayMessageGroup(message.senderId, message.content);
        chatArea.scrollTop = chatArea.scrollHeight;
        document.querySelector(`[id="${selectedGroupId}"]`).classList.add('active');


    } else {
        if (selectedUserId && selectedUserId === message.senderId) {
            displayMessage(message.senderId, message.content);
            chatArea.scrollTop = chatArea.scrollHeight;
        }

        if (selectedUserId) {
            document.querySelector(`#${selectedUserId}`).classList.add('active');
        } else {
            messageForm.classList.add('hidden');
        }

        const notifiedUser = document.querySelector(`#${message.senderId}`);
        if (notifiedUser && !notifiedUser.classList.contains('active')) {
            const nbrMsg = notifiedUser.querySelector('.nbr-msg');
            nbrMsg.classList.remove('hidden');
            nbrMsg.textContent = '';
        }
    }
}

function onLogout() {
    stompClient.send("/app/user.disconnectUser",
        {},
        JSON.stringify({nickName: nickname, fullName: fullname, status: 'OFFLINE'})
    );
    window.location.reload();
}

usernameForm.addEventListener('submit', connect, true);
groupPage.addEventListener('submit', selectGroupInfo, true);
messageForm.addEventListener('submit', sendMessage, true);
logout.addEventListener('click', onLogout, true);
window.onbeforeunload = () => onLogout();


document.getElementById('toggleListBtn').addEventListener('click', function () {
    const usersListContainer = document.getElementById('usersListContainer');
    const groupListContainer = document.getElementById('groupListContainer');
    const toggleListBtn = document.getElementById('toggleListBtn');

    if (isContactsListVisible) {
        usersListContainer.classList.add('hidden');
        groupListContainer.classList.remove('hidden');
        toggleListBtn.textContent = 'Contacts';

    } else {
        usersListContainer.classList.remove('hidden');
        groupListContainer.classList.add('hidden');
        toggleListBtn.textContent = 'Groups';
    }

    isContactsListVisible = !isContactsListVisible;
});
