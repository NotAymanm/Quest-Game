const { Builder, By, until } = require('selenium-webdriver');
const assert = require('assert');

class AdventureGameTest {
    constructor() {
        this.driver = null;
    }

    async setup() {
        this.driver = await new Builder().forBrowser('chrome').build();
        await this.driver.get('http://localhost:8081');

    }

    async teardown() {
        if(this.driver) await this.driver.quit();
    }

    async clickButton(buttonText) {
        const button = await this.driver.findElement(By.xpath(`//button[contains(text(), '${buttonText}')]`));
        await button.click();
        await this.driver.sleep(500);
    }

    async buildStages(stages){
        for(const cards of stages){
            await this.selectCards(cards);
            await this.clickButton('Build Stage');
        }
    }

    async handleDiscards(discards) {
        for(const cards of discards){
            await this.selectCards(cards);
            await this.clickButton('Discard');
        }
    }

    async handleAttacks(attacks) {
        for(const cards of attacks) {
            await this.selectCards(cards);
            await this.clickButton('Attack');
        }
    }

    async handleParticipation(participations) {
        await this.inputDecision(participations);
        await this.driver.sleep(500);
    }

    async inputDecision(decisions) {
        const yesButton = await this.driver.findElement(By.xpath("//button[contains(text(), 'Yes')]"));
        const noButton = await this.driver.findElement(By.xpath("//button[contains(text(), 'No')]"));
    
        for (let i = 0; i < decisions.length; i++) {
            await this.driver.sleep(2800);
            const yes = decisions[i];
            if(yes) await yesButton.click();
            else await noButton.click();
        }
    }

    async selectCards(cards){
        const cardClickCounts = {}; // Keep track of how many times each card has been clicked.

        for(let i = 0; i < cards.length; i++){
            const getCard = cards[i];

            // Initialize the click count for this card if not already present.
            if (!cardClickCounts[getCard]) {
                cardClickCounts[getCard] = 0;
            }

            // Get all elements matching the current card text.
            const matchingCards = await this.driver.findElements(By.xpath(`//div[contains(text(), '${getCard}')]`));

            const occurrenceToClick = cardClickCounts[getCard]; // Get the current occurrence count.
            
            if (matchingCards.length > occurrenceToClick) {
                // Click the nth occurrence of the card.
                await matchingCards[occurrenceToClick].click();
                // Increment the click count for this card.
                cardClickCounts[getCard]++;
            } else {
                throw new Error(`Not enough elements found for ${getCard} to click occurrence ${occurrenceToClick + 1}`);
            }
        }

        await this.driver.sleep(2800);
    }

    async getFirstCards(numCards){
        try {
            const xpath = `//div[@id='player-hand']/div[contains(@class, 'card')][position() <= ${numCards}]`;
            const elements = await this.driver.findElements(By.xpath(xpath));
            
            // Extract and return the cards
            const cardTexts = [];
            for (let element of elements) {
                const text = await element.getText();
                cardTexts.push(text);
            }

            return cardTexts;
        }
        catch(error){
            console.error("Error in getFirstCards:", error);
            return [];
        }
    }

    async getGameState() {
        // Find the game state container
        const gameState = await this.driver.findElement(By.id('game-state'));
        // Get all player elements
        const players = await gameState.findElements(By.className('player'));
    
        // Initialize an array to store player data
        const playerData = [];

        for (let i = 0; i < players.length; i++) {
            // Get the hand, hand-size, and num-shields text
            const handText = await players[i].findElement(By.className('hand')).getText();
            const handSizeText = await players[i].findElement(By.className('hand-size')).getText();
            const shieldsText = await players[i].findElement(By.className('num-shields')).getText();
    
            // Clean up the text by removing prefixes and convert handSize and shields to integers
            const hand = handText.replace('Hand: ', '');  // Removes "Hand: "
            const handSize = parseInt(handSizeText.replace('Hand Size: ', '').trim());
            const shields = parseInt(shieldsText.replace('Num. Shields: ', '').trim());

            playerData.push({
                hand: hand,
                handSize: handSize,
                shields: shields
            });
        }
    
        // Return the array of player data
        return playerData;
    }

    async test_A1_scenario(){
        try {
            await this.setup();

            console.log(`Starting A1_scenario...`);
            // Set the test
            await this.driver.executeScript('window.test = "A1_scenario";');

            await this.driver.sleep(1000);

            // Start the Game
            await this.clickButton('Start Game');
            
            // Player 1 declines, player 2 accepts
            await this.inputDecision([false, true]);
            await this.driver.sleep(500);
            
            // Build stages
            const stageData = [
                ["F5", "H10"],
                ["F15", "S10"],
                ["F15", "D5", "B15"],
                ["F40", "B15"]
            ];
            await this.buildStages(stageData);
            
            // Participation in Stage 1
            await this.handleParticipation([true, true, true]);
            
            // Player discards
            const discards = [["F5"], ["F5"], ["F5"]];
            await this.handleDiscards(discards);

            // Players build attacks
            const attackStage1 = [
                ["D5", "S10"],
                ["S10", "D5"],
                ["D5", "H10"]
            ];
            await this.handleAttacks(attackStage1);

            // Participation in Stage 2
            await this.handleParticipation([true, true, true]);

            const attacksStage2 = [
                ["H10", "S10"],
                ["B15", "S10"],
                ["H10", "B15"]
            ];
            await this.handleAttacks(attacksStage2);
            
            //Participation in Stage 3
            await this.handleParticipation([true, true]);

            const attacksStage3 = [
                ["L20", "H10", "S10"],
                ["B15", "S10", "L20"]
            ];
            await this.handleAttacks(attacksStage3);
            
            
            // Participation in Stage 4
            await this.handleParticipation([true, true]);
            
            const attacksStage4 = [
                ["B15", "H10", "L20"],
                ["D5", "S10", "L20", "E30"]
            ];
            await this.handleAttacks(attacksStage4);
            
            // Player 2 discards
            const discardEnd = [await this.getFirstCards(4)];
            await this.handleDiscards(discardEnd);

            // get game state data from frontend
            const players = await this.getGameState();

            const correctHands = [
                "F5, F10, F15, F15, F30, H10, B15, B15, L20",
                "F20, F20, F25, F35, D5, S10, S10, H10, H10, H10, H10, E30",
                "F5, F5, F15, F30, S10",
                "F15, F15, F40, L20"
            ];

            const correctSize = [9, 12, 5, 4];

            const correctShields = [0, 0, 0, 4];

            players.forEach((player, index) => {
                assert(player.hand.includes(correctHands[index]), `Player ${index+1}'s Hand is Incorrect.`);
                assert(player.handSize == (correctSize[index]), `Player ${index+1}'s Hand Size is Incorrect.`);
                assert(player.shields == (correctShields[index]), `Player ${index+1}'s Shields is Incorrect.`);
            });

            const gameOutput = await this.driver.findElement(By.id('game-message')).getText();
            assert(gameOutput.includes(`Player 4 has won this quest!`), `Player 4 should have won the quest!`);
            assert(!gameOutput.includes(`Player 1 has won this quest!`), `Player 1 should NOT have won the quest!`);
            assert(!gameOutput.includes(`Player 2 has won this quest!`), `Player 2 should NOT have won the quest!`);
            assert(!gameOutput.includes(`Player 3 has won this quest!`), `Player 3 should NOT have won the quest!`);

            console.log("Test A1_scenario assertion passed!");

            await this.driver.sleep(5000);

        }
        catch(error) {
            console.error("Error in A1_scenario:", error);
        }
        finally {
            await this.teardown();
        }
    }
    

    async test_2winner_game_2winner_quest(){
        try {
            await this.setup();
            console.log(`Starting 2winner_game_2winner_quest...`);
            // Set the test
            await this.driver.executeScript('window.test = "2winner_game_2winner_quest";');

            await this.driver.sleep(1000);

            // Start the Game
            await this.clickButton('Start Game');

            // Player 1 accepts
            await this.inputDecision([true]);
            await this.driver.sleep(500);

            // Build stages
            let stageData = [
                ["F5"],
                ["F5", "D5"],
                ["F10", "H10"],
                ["F10", "B15"]
            ];
            await this.buildStages(stageData);

            // Participation in Stage 1
            await this.handleParticipation([true, true, true]);

            // Player discards
            const discards = [["F5"], ["F5"], ["F10"]];
            await this.handleDiscards(discards);

            // Players build attacks
            let attacksStage1 = [
                ["H10"],
                [],
                ["H10"]
            ];
            await this.handleAttacks(attacksStage1);

            // Participation in Stage 2
            await this.handleParticipation([true, true]);

            let attacksStage2 = [
                ["S10"],
                ["S10"]
            ];
            await this.handleAttacks(attacksStage2);

            //Participation in Stage 3
            await this.handleParticipation([true, true]);

            let attacksStage3 = [
                ["H10", "S10"],
                ["H10", "S10"]
            ];
            await this.handleAttacks(attacksStage3);

            // Participation in Stage 4
            await this.handleParticipation([true, true]);

            let attacksStage4 = [
                ["S10", "B15"],
                ["S10", "B15"]
            ];
            await this.handleAttacks(attacksStage4);

            // Player 1 discards
            let discardEnd = [["F5", "F10", "F15", "F15"]];
            await this.handleDiscards(discardEnd);

            // End Turn
            await this.clickButton('End Turn');

            // Player 2 declines, Player 3 accepts
            await this.inputDecision([false, true]);
            await this.driver.sleep(500);

            // Build stages
            stageData = [
                ["F5"],
                ["F5", "D5"],
                ["F5", "H10"]
            ];
            await this.buildStages(stageData);

            // Participation in Stage 1
            await this.handleParticipation([false, true, true]);

            attacksStage1 = [
                ["D5"],
                ["D5"]
            ];
            await this.handleAttacks(attacksStage1);

            // Participation in Stage 2
            await this.handleParticipation([true, true]);

            attacksStage2 = [
                ["B15"],
                ["B15"]
            ];
            await this.handleAttacks(attacksStage2);

            // Participation in Stage 3
            await this.handleParticipation([true, true]);

            attacksStage3 = [
                ["E30"],
                ["E30"]
            ];
            await this.handleAttacks(attacksStage3);

            // Player 3 discards
            discardEnd = [["F20", "F25", "F30"]];
            await this.handleDiscards(discardEnd);

            // End Turn
            await this.clickButton('End Turn');


            // get game state data from frontend
            const players = await this.getGameState();

            const correctHands = [
                "F15, F15, F20, F20, F20, F20, F25, F25, F30, H10, B15, L20",
                "F10, F15, F15, F25, F30, F40, F50, L20, L20",
                "F20, F40, D5, D5, S10, H10, H10, H10, H10, B15, B15, L20",
                "F15, F15, F20, F25, F30, F50, F70, L20, L20"
            ];

            const correctSize = [12, 9, 12, 9];

            const correctShields = [0, 7, 0, 7];

            players.forEach((player, index) => {
                assert(player.hand.includes(correctHands[index]), `Player ${index+1}'s Hand is Incorrect.`);
                assert(player.handSize == (correctSize[index]), `Player ${index+1}'s Hand Size is Incorrect.`);
                assert(player.shields == (correctShields[index]), `Player ${index+1}'s Shields is Incorrect.`);
            });


            const gameOutput = await this.driver.findElement(By.id('game-message')).getText();
            assert(gameOutput.includes(`Winners`), `Game Should display the winners!`);
            assert(gameOutput.includes(`Player 2`), `Player 2 should be a winner!`);
            assert(gameOutput.includes(`Player 4`), `Player 4 should be a winner!`);
            assert(!gameOutput.includes(`Player 1`), `Player 1 should NOT be a winner!`);
            assert(!gameOutput.includes(`Player 3`), `Player 3 should NOT be a winner!`);


            console.log("Test 2winner_game_2winner_quest assertion passed!");
            await this.driver.sleep(5000);
        }
        catch(error) {
            console.error("Error in 2winner_game_2winner_quest:", error);
        }
        finally {
            await this.teardown();
        }
    }
    
    async test_1winner_game_with_events(){
        try {
            await this.setup();
            console.log(`Starting 1winner_game_with_events...`);
            // Set the test
            await this.driver.executeScript('window.test = "1winner_game_with_events";');

            await this.driver.sleep(1000);

            // Start the Game
            await this.clickButton('Start Game');
            
            // Player 1 accepts
            await this.inputDecision([true]);
            await this.driver.sleep(500);

            // Build stages
            let stageData = [
                ["F5"],
                ["F10"],
                ["F15"],
                ["F20"]
            ];
            await this.buildStages(stageData);

            
            // Participation in Stage 1
            await this.handleParticipation([true, true, true]);

            // Player discards
            let discards = [["F5"], ["F10"], ["F20"]];
            await this.handleDiscards(discards);

            let attacksStage1 = [
                ["S10"],
                ["S10"],
                ["S10"]
            ];
            await this.handleAttacks(attacksStage1);

            // Participation in Stage 2
            await this.handleParticipation([true, true, true]);

            let attacksStage2 = [
                ["H10"],
                ["H10"],
                ["H10"]
            ];
            await this.handleAttacks(attacksStage2);

            // Participation in Stage 3
            await this.handleParticipation([true, true, true]);

            let attacksStage3 = [
                ["B15"],
                ["B15"],
                ["B15"]
            ];
            await this.handleAttacks(attacksStage3);

            // Participation in Stage 4
            await this.handleParticipation([true, true, true]);

            let attacksStage4 = [
                ["L20"],
                ["L20"],
                ["L20"]
            ];
            await this.handleAttacks(attacksStage4);

            // Player 1 discards
            let discardEnd = [["F5", "F5", "F10", "F10"]];
            await this.handleDiscards(discardEnd);

            // End Turn
            await this.clickButton('End Turn');

            // End Turn
            await this.clickButton('End Turn');

            // await this.driver.sleep(100000);

            // Discard after prosperity
            discards = [["F5"], ["F20"], ["F5", "F10"], ["F5"]];
            await this.handleDiscards(discards);

            // End Turn
            await this.clickButton('End Turn');

            // Discard after Queens favor
            discards = [["F25", "F30"]];
            await this.handleDiscards(discards);

            // End Turn
            await this.clickButton('End Turn');

            // Player 1 accepts
            await this.inputDecision([true]);
            await this.driver.sleep(500);


            // Build stages
            let stageData2 = [
                ["F15"],
                ["F15", "D5"],
                ["F20", "D5"],
            ];
            await this.buildStages(stageData2);

            // Participation in Stage 1
            await this.handleParticipation([true, true, true]);

            // Discard
            discards = [["F5"], ["F10"], ["F20"]];
            await this.handleDiscards(discards);

            attacksStage1 = [
                ["B15"],
                ["B15"],
                ["H10"]
            ];
            await this.handleAttacks(attacksStage1);

            // Participation in Stage 2
            await this.handleParticipation([true, true]);

            attacksStage2 = [
                ["B15", "H10"],
                ["B15", "S10"],
            ];
            await this.handleAttacks(attacksStage2);

            // Participation in Stage 3
            await this.handleParticipation([true, true]);

            attacksStage3 = [
                ["L20", "S10"],
                ["E30"],
            ];
            await this.handleAttacks(attacksStage3);

            // Player 1 discards
            discardEnd = [["F15", "F15", "F15"]];
            await this.handleDiscards(discardEnd);

            // End Turn
            await this.clickButton('End Turn');

            // get game state data from frontend
            const players = await this.getGameState();

            const correctHands = [
                "F25, F25, F35, D5, D5, S10, S10, S10, S10, H10, H10, H10",
                "F15, F25, F30, F40, S10, S10, S10, H10, E30",
                "F10, F25, F30, F40, F50, S10, S10, H10, H10, L20",
                "F25, F25, F30, F50, F70, D5, D5, S10, S10, B15, L20"
            ];

            const correctSize = [12, 9, 10, 11];

            const correctShields = [0, 5, 7, 4];

            players.forEach((player, index) => {
                assert(player.hand.includes(correctHands[index]), `Player ${index+1}'s Hand is Incorrect.`);
                assert(player.handSize == (correctSize[index]), `Player ${index+1}'s Hand Size is Incorrect.`);
                assert(player.shields == (correctShields[index]), `Player ${index+1}'s Shields is Incorrect.`);
            });

            const gameOutput = await this.driver.findElement(By.id('game-message')).getText();
            assert(gameOutput.includes(`Winners`), `Game Should display the winners!`);
            assert(gameOutput.includes(`Player 3`), `Player 3 should be a winner!`);
            assert(!gameOutput.includes(`Player 1`), `Player 1 should NOT be a winner!`);
            assert(!gameOutput.includes(`Player 2`), `Player 2 should NOT be a winner!`);
            assert(!gameOutput.includes(`Player 4`), `Player 4 should NOT be a winner!`);


            console.log("Test 1winner_game_with_events assertion passed!");


            await this.driver.sleep(5000);

        }
        catch(error) {
            console.error("Error in 1winner_game_with_events:", error);
        }
        finally {
            await this.teardown();
        }
    }
    
    async test_0_winner_quest(){
        try {
            await this.setup();
            console.log(`Starting 0_winner_quest...`);
            // Set the test
            await this.driver.executeScript('window.test = "0_winner_quest";');

            await this.driver.sleep(1000);

            // Start the Game
            await this.clickButton('Start Game');

            // Player 1 accepts
            await this.inputDecision([true]);
            await this.driver.sleep(500);

            // Build stages
            let stageData = [
                ["F50", "D5", "S10", "H10", "B15", "L20"],
                ["F70", "D5", "S10", "H10", "B15", "L20"]
            ];
            await this.buildStages(stageData);

            // Participation in Stage 1
            await this.handleParticipation([true, true, true]);

            // Player discards
            let discards = [["F5"], ["F15"], ["F10"]];
            await this.handleDiscards(discards);

            let attacksStage1 = [
                ["E30"],
                [],
                []
            ];
            await this.handleAttacks(attacksStage1);


            // Player 1 discards
            let discardEnd = [["F5", "F10"]];
            await this.handleDiscards(discardEnd);

            // get game state data from frontend
            const players = await this.getGameState();

            const correctHands = [
                "F15, D5, D5, D5, D5, S10, S10, S10, H10, H10, H10, H10",
                "F5, F5, F10, F15, F15, F20, F20, F25, F30, F30, F40",
                "F5, F5, F10, F15, F15, F20, F20, F25, F25, F30, F40, L20",
                "F5, F5, F10, F15, F15, F20, F20, F25, F25, F30, F50, E30"
            ];

            const correctSize = [12, 11, 12, 12];

            const correctShields = [0, 0, 0, 0];

            players.forEach((player, index) => {
                assert(player.hand.includes(correctHands[index]), `Player ${index+1}'s Hand is Incorrect.`);
                assert(player.handSize == (correctSize[index]), `Player ${index+1}'s Hand Size is Incorrect.`);
                assert(player.shields == (correctShields[index]), `Player ${index+1}'s Shields is Incorrect.`);
            });

            const gameOutput = await this.driver.findElement(By.id('game-message')).getText();
            assert(!gameOutput.includes(`Player 4 has won this quest!`), `Player 4 should NOT have won the quest!`);
            assert(!gameOutput.includes(`Player 1 has won this quest!`), `Player 1 should NOT have won the quest!`);
            assert(!gameOutput.includes(`Player 2 has won this quest!`), `Player 2 should NOT have won the quest!`);
            assert(!gameOutput.includes(`Player 3 has won this quest!`), `Player 3 should NOT have won the quest!`);

            console.log("Test 0_winner_quest assertion passed!");

            await this.driver.sleep(5000);

        }
        catch(error) {
            console.error("Error in 0_winner_quest:", error);
        }
        finally {
            await this.teardown();
        }
    }
}

// Execute tests one by one
const test = new AdventureGameTest();

// Run each test individually
(async function runTests() {
    await test.test_A1_scenario(); // Run A1_Scenario
    await test.test_2winner_game_2winner_quest(); // Run 2winner_game_2winner_quest
    await test.test_1winner_game_with_events(); // Run 1winner_game_with_events
    await test.test_0_winner_quest(); // Run 0_winner_quest

})();