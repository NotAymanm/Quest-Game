console.log('script.js loaded');
const apiBaseUrl = window.apiBaseUrl;
window.test = null;

const selectedCards = [];
let currentStage = 0;
let numStages = 0;
let playerId = -1;

let promptPlayerIndex = 0;
let eligiblePlayers = []; // Will be populated dynamically from the backend

let currentTurnId = 1;
let currentSponsor = null;


// Get the button element by its ID
const dynamicButton = document.getElementById('dynamic-button');
const message = document.getElementById('game-message');
let currentButtonAction = () => {};


// ***************** GETTER FUNCTIONS ***********************
async function getHand(playerId) {
    try {
        const response = await fetch(`${apiBaseUrl}/game/playerHand?playerId=${playerId}`, {
            method: 'GET',
            headers: { 'Content-Type': 'application/json' }
        });
        const hand = await response.json();
        return hand;
    }
    catch (error) {
        console.error("Error in getHand:", error);
    }
}

async function getShields(playerId) {
    try {
        const response = await fetch(`${apiBaseUrl}/game/playerShields?playerId=${playerId}`, {
            method: 'GET',
            headers: { 'Content-Type': 'application/json' }
        });
        const shields = await response.json();
        return shields;
    }
    catch (error) {
        console.error("Error in getHand:", error);
    }
}

// ***************** FUNCTIONAL METHODS *********************
// Display the player's hand as clickable cards
function displayHand(hand, playerId, canClick) {
    const handContainer = document.getElementById("player-hand");
    handContainer.innerHTML = '';
    
    const nameDiv = document.getElementById("player-hand-id");
    nameDiv.textContent = `Player ${playerId}'s Hand:`;

    hand.forEach((card, index) => {
        const cardDiv = document.createElement("div");
        cardDiv.className = "card";
        cardDiv.textContent = `${card.name} (${card.type}, Value: ${card.value})`;
        if (canClick) cardDiv.onclick = () => toggleCardSelection(index, cardDiv);
        handContainer.appendChild(cardDiv);
    });

    updateState();
}

function initGameState(){
    const gameState = document.getElementById('game-state');
    gameState.style.display = "flex";

    for(let i = 1; i <= 4; i++){
        const playerContainer = document.createElement('div');
        playerContainer.id = 'player-container';

        const playerTitle = document.createElement('h4');
        playerTitle.textContent = `Player ${i}:`;
        playerContainer.appendChild(playerTitle);

        const playerDiv = document.createElement('div');
        playerDiv.classList.add('player');
        playerDiv.setAttribute('data-player-id', i);

        const handDiv = document.createElement('div');
        handDiv.classList.add('hand');

        const handSizeDiv = document.createElement('div');
        handSizeDiv.classList.add('hand-size');

        const numShieldsDiv = document.createElement('div');
        numShieldsDiv.classList.add('num-shields');

        playerDiv.appendChild(handDiv);
        playerDiv.appendChild(handSizeDiv);
        playerDiv.appendChild(numShieldsDiv);

        playerContainer.appendChild(playerDiv);

        gameState.appendChild(playerContainer);
    }

    updateState();

}

async function updateState(){
    const players = document.querySelectorAll('.player');
    
    players.forEach(async (player, index) => {
        let hand = await getHand(index+1);
        let handNames = hand.map(card => card.name);
        let handSize = hand.length;
        let shields = await getShields(index+1);

        const handDiv = player.querySelector('.hand');
        handDiv.textContent = `Hand: ${handNames.join(", ")}`;

        const handSizeDiv = player.querySelector('.hand-size');
        handSizeDiv.textContent = `Hand Size: ${handSize}`;

        const numShieldsDiv = player.querySelector('.num-shields');
        numShieldsDiv.textContent = `Num. Shields: ${shields}`;

    });
}

// Displays Current Event
function displayEvent(event) {
    const eventContainer = document.getElementById("current-event");
    eventContainer.innerHTML = `${event.type}, ${event.name}`;
}

// Display current player turn
function displayTurn() {
    const turnDiv = document.getElementById('current-turn');
    turnDiv.innerHTML = `Current Turn: Player ${currentTurnId}`;
}

// Toggle card selection when clicked
function toggleCardSelection(index, cardDiv) {
    if (selectedCards.includes(index)) {
        selectedCards.splice(selectedCards.indexOf(index), 1);
        cardDiv.classList.remove("selected");
    } else {
        selectedCards.push(index);
        cardDiv.classList.add("selected");
    }

    console.log("selectedCards:", selectedCards);
}

// display status
function updateStatus(message, color) {
    const stageInfo = document.getElementById("status-info");
    stageInfo.style.color = color;
    stageInfo.textContent = message;

    if(message == ``){
        stageInfo.style.display = 'none';
    }
    else{
        stageInfo.style.display = 'block';
    }
}

//Display sponsor
function updateSponsor(){
    const sponsorDiv = document.getElementById('current-sponsor');
    
    if(currentSponsor) {
        sponsorDiv.style.display = 'block';
    }
    else{
        sponsorDiv.style.display = 'none';
        return;
    }
    
    sponsorDiv.innerHTML = `Current Sponsor: Player ${currentSponsor}`;
}

// Display stages
function updateStage(){
    const stageDiv = document.getElementById('current-stage');
    
    if(currentSponsor) {
        stageDiv.style.display = 'block';
    }
    else{
        stageDiv.style.display = 'none';
        return;
    }
    stageDiv.innerHTML = `Current Stage: ${currentStage+1}`;
}

// ********** STARTING THE GAME *************
async function initGame() {
    try {
        const response = await fetch(`${apiBaseUrl}/game/initGame?test=${window.test}`, { method: 'POST' });
        const result = await response.text();

        console.log(result);

        message.style.color = `#fff`;

        initGameState();

        startGame();
    }
    catch (error) {
        console.error("Error in initGame:", error);
    }
}

async function startGame() {
    try {
        const response = await fetch(`${apiBaseUrl}/game/drawEvent`, { method: 'POST' });
        const event = await response.json();

        console.log("drawnEvent", event);

        message.innerHTML = `Player ${currentTurnId}'s turn started.`;
        displayTurn();

        if (event) displayEvent(event);
        if (event.type == "Quest") {
            getEligibleSponsors();
        }
        else if (event.type == "Event") {
            processEvent();
        }
    }
    catch (error) {
        console.error("Error in startGame:", error);
    }
}

async function gameOver() {
    try{
        const response = await fetch(`${apiBaseUrl}/game/displayWinners`, {method : 'POST'});
        const winners = await response.json();
    
        message.innerHTML = ``;
        message.style.color = `#80e675`;
    
        message.innerHTML += `Game Over!<br>Winners: `;
    
        winners.forEach((winner, index) => {
            message.innerHTML += ` Player ${winner.id}`
            if(index < winners.length-1) message.innerHTML += `,`;
        });

        dynamicButton.innerHTML = "New Game";
        dynamicButton.style.backgroundColor = '#fab246';
        currentButtonAction = initGame;
    }
    catch(error){
        console.error("Error in gameOver:", error);
    }
}

async function nextTurn() {
    try {
        const response = await fetch(`${apiBaseUrl}/game/nextTurn`, { method: 'POST', headers: { 'Content-Type': 'application/json' } })
        const result = await response.json();

        //clear status
        updateStatus(``, '#448c27');

        if (result.winners) {
            gameOver(result.winners);
            return;
        }
        if (result.nextPlayer) {
            displayHand(await getHand(result.nextPlayer.id), result.nextPlayer.id, false);
            currentTurnId = result.nextPlayer.id;
        }

        startGame();
    }
    catch (error) {
        console.error("Error in nextTurn:", error);
    }
}

async function endTurn(plus = false) {
    await checkForCardDiscard();

    displayHand(await getHand(currentTurnId), currentTurnId, false);

    currentSponsor = null;
    updateSponsor();
    updateStage();

    if(plus){
        message.innerHTML += `<br>Player ${currentTurnId}'s turn is over, End Turn.`;
    }
    else{
        message.innerHTML = `Player ${currentTurnId}'s turn is over, End Turn.`;
    }

    dynamicButton.innerHTML = "End Turn";
    dynamicButton.style.backgroundColor = '#2196f3';
    currentButtonAction = nextTurn;
}

async function checkForCardDiscard(display = false) {
    try {
        const response = await fetch(`${apiBaseUrl}/game/checkForDiscard`, { method: 'POST' });
        const playersNeedsDiscardArray  = await response.json();
        
        // if a players needs to discard
        if (playersNeedsDiscardArray.length !== 0) {
            console.log("discard:", playersNeedsDiscardArray);
            let tempPlayerId = parseInt(Object.keys(playersNeedsDiscardArray[0])[0], 10);
            let numToDiscard = playersNeedsDiscardArray[0][tempPlayerId];

            console.log("PlayerID:", tempPlayerId);
            console.log("Num to Discard:", numToDiscard);
            
            updateStatus(`Player ${tempPlayerId}, Select ${numToDiscard} cards to discard.`, '#e35959');
            
            if(!display) displayHand(await getHand(tempPlayerId), tempPlayerId, true);
            dynamicButton.innerHTML = "Discard";
            dynamicButton.style.backgroundColor = '#2196f3';
            await waitForDiscardAction(tempPlayerId);
        }

    }
    catch (error) {
        console.error("Error at checkForCardDiscard", error);
    }
}

// A function that returns a Promise which resolves when the discard action is triggered
function waitForDiscardAction(tempPlayerId) {
    return new Promise((resolve) => {
        // Directly resolve the promise once the button action is called
        currentButtonAction = async () => {
            await discardCards(tempPlayerId);  // Proceed discard
            resolve();  // Resolve once the action is triggered
        };
    });
}

async function discardCards(playerId) {
    try {
        const response = await fetch(`${apiBaseUrl}/game/discardCards?playerId=${playerId}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(selectedCards)
        });
        const result = await response.json();
        let display = true;

        if(result){
            selectedCards.length = 0; // Reset Selection
            display = false;
            updateStatus(``, "#e35959");
        }
        await checkForCardDiscard(display); // Check if cards have been discarded
    }
    catch(error){
        console.error("Error in discardCards", error);
    }
}

// ********** PROCESS EVENT **********************
async function processEvent() {
    try {
        const response = await fetch(`${apiBaseUrl}/game/processEvent`, { method: 'POST' });
        const eventOutcome = await response.json();

        message.innerHTML += `<br>${eventOutcome.message}`;
        console.log("processEvent:", eventOutcome);

        await checkForCardDiscard();
        updateStatus(``, '#448c27');

        displayHand(await getHand(eventOutcome.player.id), eventOutcome.player.id, false);

        //If its a plague don't clear message
        let plus = false;
        if(eventOutcome.message.includes("Plague")) plus = true;

        endTurn(plus);
    }
    catch (error) {
        console.error("Error in processEvent:", error);
    }
}

// ************ HANDLING SPONSOR QUEST ************
async function getEligibleSponsors() {
    try {
        const response = await fetch(`${apiBaseUrl}/game/determineEligibleSponsors`, { method: 'POST', headers: { 'Content-Type': 'application/json' } });
        const players = await response.json();

        console.log("EligibleSponsors:", players);

        eligiblePlayers = players;
        promptPlayerIndex = 0; // Reset the index
        promptNextSponsor(eligiblePlayers[promptPlayerIndex]); // Start the sequence
    }
    catch (error) {
        console.error("Error in getEligibleSponsors:", error);
    }
}

async function promptNextSponsor(player) {

    if (promptPlayerIndex >= eligiblePlayers.length) {
        console.log('All players have been prompted');
        endTurn();
        return; // All players have been prompted
    }

    displayHand(await getHand(player.id), player.id, false);

    if(player.id == currentTurnId){
        message.innerHTML += `<br>Player ${player.id}, would you like to sponsor this Quest?`;
    }
    else{
        message.innerHTML = `Player ${player.id}, would you like to sponsor this Quest?`;
    }

    const buttons = document.getElementById('buttons');

    dynamicButton.innerHTML = 'Yes'
    dynamicButton.style.backgroundColor = '#17a63d';
    currentButtonAction = () => handleSponsorDecision(1);

    let noButton = document.querySelector('.no-button');

    if(!noButton){
        noButton = document.createElement('button');
        noButton.innerHTML = 'No';
        noButton.className = 'no-button';
        noButton.onclick = () => handleSponsorDecision(0); // 0 = No
    }

    buttons.appendChild(noButton);
}


// Function to handle the decision for the current player
async function handleSponsorDecision(decision) {

    try {
        const response = await fetch(`${apiBaseUrl}/game/promptNextSponsor`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                playerIndex: promptPlayerIndex + 1, // Increment to prompt the next player
                decision: decision // Send the current player's decision
            })
        });
        const result = await response.json();

        console.log("sponsorDecision:", result.message);

        // Remove no button
        const buttonToRemove = document.querySelector('.no-button');
        
        if (result.sponsored == "yes") {
            //update sponsor
            currentSponsor = result.sponsorId;
            updateSponsor();
            if (buttonToRemove) buttonToRemove.remove();
            getQuest();
        }
        else if (result.nextPlayer) {
            promptPlayerIndex++; // Move to next Player
            promptNextSponsor(eligiblePlayers[promptPlayerIndex]); // Ask the next player
        }
        else {
            if (buttonToRemove) buttonToRemove.remove();
            console.log("Everyone declined to sponsor.");
            nextTurn();
        }
    }
    catch (error) {
        console.error("Error in handleSponsorDecision:", error);
    }
}


// ********** HANDLING QUEST BUILDING *************
// get the quest and numStages from the backend
async function getQuest() {
    try {
        const response = await fetch(`${apiBaseUrl}/game/getQuest`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' }
        });
        const data = await response.json();
        console.log("GetQuest:", data);

        startStageBuilding(data.numStages, data.player);
    }
    catch (error) {
        console.error("Error in getQuest:", error);
    }
}



async function startStageBuilding(stages, player) {
    numStages = stages;  // Set numStages dynamically
    playerId = player; // Set player dynamically
    currentStage = 0;  // Reset the stage counter
    message.innerHTML = `Player ${player} Will Sponsor Q${stages}.`
    updateStage();
    updateStatus(`Player ${playerId} Building Stage ${currentStage + 1} of ${numStages}`, '#448c27');
    displayHand(await getHand(playerId), playerId, true);

    //updates button to ask for finishStageBuilding
    currentButtonAction = finishStageBuilding;
    dynamicButton.style.backgroundColor = '#2196f3';
    dynamicButton.innerHTML = 'Build Stage'
}

// Finish stage and send selected cards to backend
async function finishStageBuilding() {
    try {
        const response = await fetch(`${apiBaseUrl}/game/buildStage?playerId=${playerId}&stageNumber=${currentStage}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(selectedCards),
        });
        const result = await response.text();

        console.log("buildStage:", result);

        message.innerHTML = result;

        //If stage is invalid, return
        if (!result.includes("Stage is valid.")) return;

        selectedCards.length = 0; // Clear selection for next stage
        currentStage++;
        if (currentStage < numStages) {
            updateStatus(`Player ${playerId} Building Stage ${currentStage + 1} of ${numStages}`, '#448c27');
            displayHand(await getHand(playerId), playerId, true);
            updateStage();
        } else {
            updateStatus(`Player ${playerId} Stage Building Complete`, '#448c27');
            currentStage = 0;
            updateStage();
            getEligibleParticipants();
        }
    }
    catch (error) {
        console.error("Error in finishStageBuilding:", error);
    }
}

// ********** HANDLING PARTICIPANTS *************
async function getEligibleParticipants() {
    try {
        const response = await fetch(`${apiBaseUrl}/game/determineEligibleParticipants`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ stage: currentStage })
        });
        const players = await response.json();

        eligiblePlayers = players;
        promptPlayerIndex = 0; // Reset the index
        await promptNextParticipant(players[promptPlayerIndex]); // Start the sequence
    }
    catch (error) {
        console.error("Error in getEligibleParticipants:", error);
    }
}

async function promptNextParticipant(player) {

    if (promptPlayerIndex >= eligiblePlayers.length) {
        console.log('All players have been prompted');
        endQuest();
        return; // All players have been prompted
    }

    displayHand(await getHand(player.id), player.id, false);

    message.innerHTML = `Player ${player.id}, would you like to participate in this stage?`;

    const buttons = document.getElementById('buttons');

    dynamicButton.innerHTML = 'Yes';
    dynamicButton.style.backgroundColor = '#17a63d';
    currentButtonAction = () => handleParticipantDecision(1); // 1 = Yes

    let noButton = document.querySelector('.no-button');

    if(!noButton){
        noButton = document.createElement('button');
        noButton.innerHTML = 'No';
        noButton.className = 'no-button';
        noButton.onclick = () => handleParticipantDecision(0); // 0 = No
    }

    buttons.appendChild(noButton);
}

// Function to handle the decision for the current player
async function handleParticipantDecision(decision) {
    try {
        // Send the decision for the current player to the backend
        const response = await fetch(`${apiBaseUrl}/game/promptNextParticipant`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                playerIndex: promptPlayerIndex + 1, // Increment to prompt the next player
                decision: decision // Send the current player's decision
            })
        });
        const data = await response.json();

        // Remove no button
        const buttonToRemove = document.querySelector('.no-button');
        
        const nextPlayer = data.nextPlayer;
        eligiblePlayers = data.participants; // Update participants to reflect backend
        
        if (nextPlayer) {
            promptPlayerIndex = data.currentIndex; // Move to the next player
            promptNextParticipant(nextPlayer); // Ask the next player
        } else {
            message.innerHTML = `All Eligible Players Have Been Prompted to Participate`;
            if (buttonToRemove) buttonToRemove.remove();
            await participantsDraw();
        }
    }
    catch (error) {
        console.error("Error in handleParticipantsDecision:", error);
    }
}

async function participantsDraw() {
    try {
        const response = await fetch(`${apiBaseUrl}/game/participantsDraw`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' }
        });
        const participantsDrew = await response.json();

        console.log("participantsDrew:", participantsDrew);

        await checkForCardDiscard();

        dynamicButton.style.backgroundColor = '#2196f3';
        
        if (participantsDrew) {
            promptPlayerIndex = 0; // Reset index

            selectedCards.length = 0; // Reset selected Cards
            displayHand(await getHand(eligiblePlayers[promptPlayerIndex].id), eligiblePlayers[promptPlayerIndex].id, true);

            //TELL PLAYER TO ATTACK
            message.innerHTML = `Player ${eligiblePlayers[promptPlayerIndex].id}, build your attack for stage ${currentStage+1}.`
            updateStatus(`Playing Stage ${currentStage+1} of ${numStages}`, '#448c27');

            // UDPATES BUTTON TO BUILD ATTACK
            currentButtonAction = resolveAttack;
            dynamicButton.innerHTML = 'Attack';
        }
        else {
            //No one participated.
            await endQuest();
        }
    }
    catch (error) {
        console.error("Error in participantsDraw:", error);
    }
}

// ************ HANDLE ATTACK ****************
async function resolveAttack() {
    try{
        // Send attack data to the backend
        const response = await fetch(`${apiBaseUrl}/game/resolveAttacks?stage=${currentStage}&playerIndex=${promptPlayerIndex}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(selectedCards),
        });
        const result = await response.json();

        
        // Handle the result
        if (result.attackStatus === "invalid") {
            console.log("Invalid attack:", result.message);
            message.innerHTML = result.message;
            return;
        }
    
        selectedCards.length = 0; // reset selectedCards
    
        console.log("Valid Attack:", result.message);
        eligiblePlayers = result.remainingParticipants;
    
        console.log("PromptPlayerIndex Before:", promptPlayerIndex)
        console.log("eligiblePlayers length:", eligiblePlayers.length);
        // Prompt the next player
        promptPlayerIndex = result.currentIndex + 1;
        console.log("PromptPlayerIndex After:", promptPlayerIndex);
    
        //if all players have been prompted
        if(promptPlayerIndex >= eligiblePlayers.length){
            
            promptPlayerIndex = 0; // Reset player index
            
            //If there is still stages to go to, go to next stage
            if((currentStage+1) < numStages){
                currentStage++;
                updateStatus(`Playing Stage ${currentStage+1} of ${numStages}`, '#448c27');
                updateStage();
                promptNextParticipant(eligiblePlayers[promptPlayerIndex]); // Start the sequence
                return;
            }
            
            //else if there is no more stages to go to
            currentButtonAction = () => {}

            //Then Pay Winners
            await payWinners();

            //And End Quest
            await endQuest();
            return;
            
        }
    
        displayHand(await getHand(eligiblePlayers[promptPlayerIndex].id), eligiblePlayers[promptPlayerIndex].id, true);
        message.innerHTML = `Player ${eligiblePlayers[promptPlayerIndex].id}, build your attack for stage ${currentStage+1}.`
    }
    catch(error){
        console.error("Error in resolveAttack", error);
    }

}

async function payWinners() {
    try{
        const response = await fetch(`${apiBaseUrl}/game/payWinners?numStages=${numStages}`, {method: 'POST'});
        const result = await response.json();

        console.log("payWinners:", result);

        message.innerHTML = ``;

        //If there are winners output them
        if(result.length > 0){
            result.forEach((player, index) => {
                if (index > 0) message.innerHTML += `<br>`;
                message.innerHTML += `Player ${player.id} has won this quest!`;
            });
        }
        else{
            message.innerHTML += `No one won this Quest.`;
        }
    }
    catch(error){
        console.error("Error in payWinners:", error);
    }
}

async function endQuest() {
    try{
        const response = await fetch(`${apiBaseUrl}/game/endQuest`, {method: 'POST'});
        await endTurn(true);
    }
    catch(error){
        console.error("Error in endQuest:", error);
    }
    
}

let startGameButton = document.getElementById("start-game");
startGameButton.addEventListener('click', function() {
    let showGame = document.getElementById("main-game");
    showGame.style.display = "block";
    startGameButton.style.display = "none";

    initGame();
});