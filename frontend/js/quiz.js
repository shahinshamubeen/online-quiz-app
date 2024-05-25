// Extracting topic and difficulty from the URL parameters
const urlParams = new URLSearchParams(window.location.search);
const topicParam = urlParams.get('topic');
const difficultyParam = urlParams.get('difficulty');

// Initializing necessary variables
const questions = [];
const answers = [];
let currentQuestionIndex = 0;
let time = 30;
let timers = [];
let selections = [];
let timer; // Declare timer variable

// Display topic and difficulty on the UI
document.getElementById('topic').textContent = topicParam;
document.getElementById('difficulty').textContent = `(Level: ${difficultyParam})`;

// Function to start the timer for the current question
function startTimer() {
    clearInterval(timer); // Clear any existing timer

    // Check if the answers list is empty to determine if the quiz is still ongoing
    if (answers.length === 0) {
        timer = setInterval(() => {
            if (timers[currentQuestionIndex] > 0) {
                timers[currentQuestionIndex]--;
                document.getElementById('time').textContent = timers[currentQuestionIndex];
                document.getElementById('time').style.color = 'green';
            } else {
                clearInterval(timer); // Stop the timer
                document.getElementById('time').textContent = '0';
                document.getElementById('time').style.color = 'red';
                loadQuestion(currentQuestionIndex); // Reload question if timer reaches zero
                return;
            }
        }, 1000);
    }
}

// Function to load a question based on its index
function loadQuestion(index) {
    const question = questions[index];
    currentQuestionIndex = index;
    const id = question.id;
    document.getElementById('question').textContent = question.question;
    const optionsContainer = document.getElementById('options');
    optionsContainer.innerHTML = ''; // Clear previous options

    const selection = selections.find(selection => selection.id === id);
    const answer = answers.find(answer => answer.id === id);
    const fragment = document.createDocumentFragment(); // Create a document fragment

    // Generate options for the question
    question.options.forEach((optionText, i) => {
        const button = document.createElement('button');
        button.classList.add('option-button');
        button.textContent = optionText;

        if (answers.length === 0) { // During quiz
            button.onclick = () => selectOption(optionText);

            if (selection && selection.selected === optionText) {
                button.style.backgroundColor = 'darkblue'; // Highlight selected option
            } else {
                button.style.backgroundColor = ''; // Remove highlight
            }

            if (currentQuestionIndex === index && timers[currentQuestionIndex] === 0) {
                button.disabled = true; // Disable options only for the current question when its timer reaches zero
                button.style.opacity = 0.7; // Change opacity to indicate the option is disabled
                button.style.pointerEvents = 'none'; // Disable pointer events
            } else {
                button.disabled = false; // Enable options for other questions or when the timer is not zero
                button.style.opacity = 1; // Change opacity to indicate the option is enabled
                button.style.pointerEvents = 'auto'; // Enable pointer events
            }
        } else { // After quiz
            button.disabled = true; // Disable options
            button.style.pointerEvents = 'none'; // Disable pointer events

            if (answer && answer.answer === optionText) {
                if (selection && selection.selected === optionText) {
                    button.style.backgroundColor = 'green';
                    button.style.border = '3px solid darkblue';
                } else {
                    button.style.backgroundColor = 'green'; // Highlight correct answer
                }
            } else {
                if (selection && selection.selected === optionText) {
                    button.style.backgroundColor = 'red'; // Highlight wrong answer
                } else {
                    button.style.backgroundColor = ''; // Default background
                }
            }
        }

        fragment.appendChild(button); // Append button to the fragment
    });

    // Update feedback based on the selection and answer
    if (selection) {
        if (answer && answer.answer === selection.selected) {
            document.getElementById('feedback').textContent = 'Correct Answer';
            document.getElementById('feedback').style.color = 'green';
        } else {
            document.getElementById('feedback').textContent = 'Wrong Answer';
            document.getElementById('feedback').style.color = 'red';
        }
    } else {
        document.getElementById('feedback').textContent = 'Not Answered';
        document.getElementById('feedback').style.color = 'black';
    }

    optionsContainer.appendChild(fragment); // Append fragment to the container in one go
    updateNavigationButtons();
    updateQuestionNumber();
    updateQuestionNumbers();
    startTimer();
}

// Function to handle option selection
function selectOption(option) {
    const id = questions[currentQuestionIndex].id;
    const selected = option; // The option text itself

    const existingIndex = selections.findIndex(item => item.id === id);
    if (existingIndex !== -1) {
        if (selections[existingIndex].selected === selected) {
            // Remove the selection if the same option is selected again
            selections.splice(existingIndex, 1);
        } else {
            // Update the selection if a different option is selected
            selections[existingIndex].selected = selected;
        }
    } else {
        // Add the selection if it doesn't exist
        selections.push({ id: id, selected: selected });
    }

    loadQuestion(currentQuestionIndex); // Reload question to update the UI
}

// Function to update navigation buttons' state
function updateNavigationButtons() {
    const prevButton = document.getElementById('prev-btn');
    const nextButton = document.getElementById('next-btn');
    prevButton.disabled = currentQuestionIndex === 0; // Disable previous button on first question
    nextButton.disabled = currentQuestionIndex === questions.length - 1; // Disable next button on last question
}

// Function to update the current question number display
function updateQuestionNumber() {
    const questionNumber = document.getElementById('question-number');
    questionNumber.textContent = `Question ${currentQuestionIndex + 1} of ${questions.length}`;
}

// Function to update the question numbers in the navigation
function updateQuestionNumbers() {
    const questionNumbersContainer = document.getElementById('question-numbers');
    questionNumbersContainer.innerHTML = ''; // Clear previous question numbers

    questions.forEach((_, index) => {
        const button = document.createElement('button');
        button.classList.add('question-number-button');
        button.textContent = index + 1;
        button.onclick = () => loadQuestion(index);

        if (currentQuestionIndex === index) {
            button.classList.add('active'); // Highlight the current question number
        }

        const selection = selections.find(selection => selection.id === questions[index].id);
        const answer = answers.find(answer => answer.id === questions[index].id);

        // Update feedback based on the selection and answer
        if (selection) {
            if (answer && answer.answer === selection.selected) {
                button.classList.add('answered-correct'); // Highlight the current question number
            } else if (answer && answer.answer !== selection.selected) {
                button.classList.add('answered-wrong'); // Highlight the current question number
            } else {
                button.classList.add('answered'); // Highlight the current question number
            }
        }

        questionNumbersContainer.appendChild(button);
    });
}

// Function to navigate to the previous question
function prevQuestion() {
    if (currentQuestionIndex > 0) {
        currentQuestionIndex--;
        loadQuestion(currentQuestionIndex);
    }
}

// Function to navigate to the next question
function nextQuestion() {
    if (currentQuestionIndex < questions.length - 1) {
        currentQuestionIndex++;
        loadQuestion(currentQuestionIndex);
    }
}

// Function to fetch questions from the server
function getQuestions() {
    fetch('http://localhost:8080/api/quiz/questions', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        // Sending topic and difficulty obtained from URL parameters in the request body
        body: JSON.stringify({ topic: topicParam, difficulty: difficultyParam })
    })
    .then(response => response.json())
    .then(data => {
        console.log('Response from /api/quest:', data);
        data.forEach(question => {
            questions.push({
                id: question.id,
                question: question.question,
                options: question.options
            });
            timers.push(time);
        });
        loadQuestion(currentQuestionIndex);
    })
    .catch(error => console.error('Error:', error));
}

// Event listener for quitting the quiz
document.getElementById('quit').addEventListener('click', function() {
    window.location.href = '/home.html'; // Redirect to home page
});

// Event listener for trying the quiz again
document.getElementById('try-again').addEventListener('click', function() {
    document.getElementById("result-box").style.display = "none";
    document.getElementById("feedback").style.display = "none";

    document.getElementById("timer").style.display = "block";
    document.getElementById("finish").style.display = "block";

    document.getElementById('attempted').textContent = ``;

    timers = new Array(questions.length).fill(time);

    selections.length = 0;
    answers.length = 0;
    loadQuestion(0);
});

// Event listener for finishing the quiz
document.getElementById('finish').addEventListener('click', function() {
    document.getElementById("result-box").style.display = "block";
    document.getElementById("feedback").style.display = "block";

    document.getElementById("timer").style.display = "none";
    document.getElementById("finish").style.display = "none";

    document.getElementById('attempted').textContent = `Attempted : ${selections.length} out of ${questions.length}`;

    fetch('http://localhost:8080/api/quiz/answers', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ topic: topicParam, difficulty: difficultyParam })
    })
    .then(response => response.json())
    .then(data => {
        console.log('Response from /api/answers:', data);
        data.forEach(answer => {
            answers.push({
                id: answer.id,
                answer: answer.answer,
            });
        });

        console.log('updated answers:', data);
        calculateMarks();

        loadQuestion(0);
    })
    .catch(error => console.error('Error:', error));
});

// Function to calculate and display the total score
function calculateMarks() {
    let correctMatches = 0; // Use a variable to store correct matches

    for (const selection of selections) {
        const matchingAnswer = answers.find(answer => answer.id === selection.id);

        if (matchingAnswer && matchingAnswer.answer === selection.selected) {
            correctMatches++; // Increment on a correct match
        }
    }
    document.getElementById('result').textContent = `Your Total Score is : ${correctMatches} out of ${questions.length}`;

    console.log("Correct matches:", correctMatches);
}

// Fetch questions when the DOM content is loaded
document.addEventListener('DOMContentLoaded', () => {
    getQuestions();
});

// Save selected options in local storage before the window unloads
window.addEventListener('beforeunload', () => {
    localStorage.setItem('selectedOptions', JSON.stringify(questions)); // Save selected options in local storage
});
