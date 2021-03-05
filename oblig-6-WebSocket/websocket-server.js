const net = require('net');
const crypto = require('crypto');
const { Console } = require('console');

// Simple HTTP server responds with a simple WebSocket client test
const httpServer = net.createServer((connection) => {
    connection.on('data', () => {
        let content = 
        `
        <!DOCTYPE html>
        <html>
            <head>
                <meta charset="UTF-8" />
            </head>
            <body>
                WebSocket test page
                <script>
                    let ws = new WebSocket('ws://localhost:3001');
                    ws.onmessage = event => alert('Message from server: ' + event.data);
                    ws.onopen = () => ws.send('hello');
                </script>
            </body>
        </html>
        `;
        connection.write('HTTP/1.1 200 OK\r\nContent-Length: ' + content.length + '\r\n\r\n' + content)
    });
});

httpServer.listen(3000, () => {
    console.log('HTTP server listening on port 3000');
})


const wsServer = net.createServer((connection) => {
    console.log('Client connected');

    // What the server should do when getting data form a websocket client
    connection.on('data', (data) => {
        console.log('Data recieved from client: ', data.toString());
        if(data.toString().includes("Sec-WebSocket-Key:")){
            let sha64key = handleClientHandshake(data.toString());
            connection.write("HTTP/1.1 101 Switching Protocols\r\n");
            connection.write("Upgrade: websocket\r\n");
            connection.write("Connection: Upgrade\r\n");
            connection.write("Sec-WebSocket-Accept: " + sha64key + "\r\n\r\n");
        }else{
            decryptMessage(data);
        }
    });

    // What the server should do when client ends the connection
    connection.on('end', () => {
        console.log('Client disconnected');
    });

});

// Handle error
wsServer.on('error', (error) => {
    console.log('Error: ', error);
});

// Open websocket server and listen on port 3001
wsServer.listen(3001, () => {
    console.log('WebSocket server listening on port 3001');
});


function handleClientHandshake(handshakeReq){
    let dataArr = handshakeReq.toString().split("\n");
    for(let i = 0; i < dataArr.length; i ++){
        dataArr[i] = dataArr[i].toString().split(":");
    }
    let secWebSocketKey;
    let serverSocketConstKey = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";

    for(let i = 0; i < dataArr.length; i ++){
        console.log(dataArr[i][0].toString().trim());
        if(dataArr[i][0].toString().trim() === "Sec-WebSocket-Key"){
            secWebSocketKey = dataArr[i][1].toString().trim();
        }
    }

    let magicKey = secWebSocketKey.concat(serverSocketConstKey);
    let magicKeyHashedAndBase64 = hashAndBase64(magicKey);
    console.log(magicKey);
    console.log(magicKeyHashedAndBase64);

    return magicKeyHashedAndBase64;
}


function hashAndBase64(input){
    return crypto.createHash('sha1').update(input).digest('base64');
}

function decryptMessage(data){
    //Første byte angir type melding.
    let bytes = Buffer.from(data);

    //Første bit byte nr 2 angir om meldingen er maskert eller ikke
    //De 7 resten angir lengden på meldingen
    let length = bytes[1] & 127;
    console.log("Length: " + length);

    //Masken starter fra 2 byte og er 4 bytes lang. Dette gjelder alltid
    let maskStart = 2;
    let dataStart = maskStart + 4;
    console.log("Maskstart and datastart: 2 and " + dataStart);

    let decodedMessage = "";
    for(let i = dataStart; i < dataStart + length; i ++){
        let byte = bytes[i] ^ bytes[maskStart + ((i - dataStart) % 4)]
        console.log(String.fromCharCode(byte));    
    }
    console.log(decodedMessage);
}