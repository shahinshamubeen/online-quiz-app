document.addEventListener('DOMContentLoaded', function() {
    checkBackendConnection();


    function checkBackendConnection(){
        fetch('http://localhost:8080/api/greet')
        .then(response => response.text())
        .then(data => {
            console.log('Response from backend:', data);
        })
        .catch(error => console.error('Error:', error));

    }
});
