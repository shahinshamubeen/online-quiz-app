// Wait for the DOM content to be loaded before executing any JavaScript
document.addEventListener('DOMContentLoaded', function () {
    // Call the onPageLoad function when the DOM content is loaded
    onPageLoad();

    // Function to be executed when the DOM content is loaded
    function onPageLoad() {
        // Get the container where topics will be displayed
        const responseDiv = document.getElementById('topics');

        // Fetch topics from the server and render them on the page
        fetchTopics()
            .then(data => renderTopics(data, responseDiv)) // Render topics on successful fetch
            .catch(error => console.error('Error:', error)); // Log error if fetch fails
    }

    // Function to fetch topics from the server
    async function fetchTopics() {
        // Fetch topics from the server
        const response = await fetch('http://localhost:8080/api/quiz/topics');
        // Check if the fetch operation was successful
        if (!response.ok) {
            // If fetch fails, throw an error
            throw new Error('Failed to fetch topics');
        }
        // Return the parsed JSON response
        return await response.json();
    }

    // Function to render topics on the page
    function renderTopics(topics, responseDiv) {
        // Clear the container before rendering new topics
        responseDiv.innerHTML = '<h2>Select topic difficulty</h2>';

        // Initialize a counter for numbering topics
        let topicCounter = 1;

        // Loop through each topic in the topics array
        topics.forEach(topic => {
            // Create a new div for each topic
            const topicDiv = document.createElement('div');
            topicDiv.classList.add('topic');

            // Create a heading for the topic with numbering
            const topicHeading = document.createElement('h3');
            topicHeading.textContent = `${topicCounter}. ${topic.topic}`;
            topicDiv.appendChild(topicHeading);

            // Loop through each difficulty for the topic
            topic.difficulties.forEach(difficulty => {
                // Create a button for each difficulty
                const difficultyBtn = createDifficultyButton(topic.topic, difficulty);
                // Append the difficulty button to the topic div
                topicDiv.appendChild(difficultyBtn);
            });

            // Increment the topic counter for numbering
            topicCounter++;
            // Append the topic div to the container
            responseDiv.appendChild(topicDiv);
        });
    }

    // Function to create a difficulty button
    function createDifficultyButton(topic, difficulty) {
        // Create a new button element
        const difficultyBtn = document.createElement('button');
        // Add CSS class to the button for styling
        difficultyBtn.classList.add('difficulty-btn');
        // Set the text content of the button to the difficulty level
        difficultyBtn.textContent = difficulty;
        // Add event listener to the button for handling clicks
        difficultyBtn.addEventListener('click', () => redirectToQuiz(topic, difficulty));
        // Return the created button
        return difficultyBtn;
    }

    // Function to redirect to the quiz page with selected topic and difficulty
    function redirectToQuiz(topic, difficulty) {
        // Encode topic and difficulty to be passed as query parameters
        const encodedTopic = encodeURIComponent(topic);
        const encodedDifficulty = encodeURIComponent(difficulty);
        // Redirect to the quiz page with encoded topic and difficulty
        window.location.href = `quiz.html?topic=${encodedTopic}&difficulty=${encodedDifficulty}`;
    }
});
