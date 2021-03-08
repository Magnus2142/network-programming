const net = require('net');
const crypto = require('crypto');
const static = require('node-static');
const file = new static.Server('./');
const http = require('http');

const httpServer = http.createServer((req, res) => {
    req.addListener('end', () => file.serve(req, res)).resume();
});

httpServer.listen(3000, () => {
    console.log('HTTP server listening on port 3000');
})


const connectedSockets = new Set();

connectedSockets.broadcast = function(data, except) {
    for(let connection of this){
        if(connection != except){
            connection.write(data);
        }
    }
}

const wsServer = net.createServer((connection) => {
    connectedSockets.add(connection);
    console.log('Client connected');
    console.log('There are now ' + connectedSockets.size + ' clients connected');

    // What the server should do when getting data form a websocket client
    connection.on('data', (data) => {
        if(isUpgradeRequest(data)){
            let secWebSocketKey = getSecWebSocketKey(data);
            let acceptKey = hashAndBase64(secWebSocketKey);

            connection.write("HTTP/1.1 101 Web Socket Protocol Handshake\r\n");
            connection.write("Upgrade: websocket\r\n");
            connection.write("Connection: Upgrade\r\n");
            connection.write("Sec-WebSocket-Accept: " + acceptKey + "\r\n\r\n");

            console.log("Upgraded to websocket!");
        }else{
            const message = decryptMessage(data);
            console.log(message);

            let response = constructReply(message);
            connectedSockets.broadcast(response, connection);
        }
    });

    // What the server should do when client ends the connection
    connection.on('end', () => {
        connectedSockets.delete(connection);
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



/*-----------------------------------------------------------------------------------

                                UTILITY FUNCTIONS

------------------------------------------------------------------------------------*/

function isUpgradeRequest(data){
    let req = data.toString().split("\n");
    for(let i = 0; i < req.length; i ++){
        let tmp = req[i].split(":");
        if(tmp[0].trim() === "Sec-WebSocket-Key"){
            return true;
        }
    }
    return false;
}

function getSecWebSocketKey(data){
    let req = data.toString().split("\n");
    for(let i = 0; i < req.length; i ++){
        let tmp = req[i].split(":");
        if(tmp[0].trim() === "Sec-WebSocket-Key"){
            return tmp[1].trim();
        }
    }
}

function hashAndBase64(input){
    return crypto.createHash('sha1').update(input + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11", "binary").digest('base64');
}

function constructReply (data) {
    // Convert the data to JSON and copy it into a buffer
    const json = JSON.stringify(data)
    const jsonByteLength = Buffer.byteLength(json);
    // Note: we're not supporting > 65535 byte payloads at this stage 
    const lengthByteCount = jsonByteLength < 126 ? 0 : 2; 
    const payloadLength = lengthByteCount === 0 ? jsonByteLength : 126; 
    const buffer = Buffer.alloc(2 + lengthByteCount + jsonByteLength); 
    // Write out the first byte, using opcode `1` to indicate that the message 
    // payload contains text data 
    buffer.writeUInt8(0b10000001, 0); 
    buffer.writeUInt8(payloadLength, 1); 
    // Write the length of the JSON payload to the second byte 
    let payloadOffset = 2; 
    if (lengthByteCount > 0) { 
      buffer.writeUInt16BE(jsonByteLength, 2); payloadOffset += lengthByteCount; 
    } 
    // Write the JSON data to the data buffer 
    buffer.write(json, payloadOffset); 
    return buffer;
  }

function decryptMessage(data){
    //Første byte angir type melding.
    let bytes = Buffer.from(data);

    //Første bit byte nr 2 angir om meldingen er maskert eller ikke
    //De 7 resten angir lengden på meldingen
    let length = bytes[1] & 127;
    //console.log("Length: " + length);

    //Masken starter fra 2 byte og er 4 bytes lang. Dette gjelder alltid
    let maskStart = 2;
    let dataStart = maskStart + 4;
    //console.log("Maskstart and datastart: 2 and " + dataStart);

    let decodedMessage = "";
    for(let i = dataStart; i < dataStart + length; i ++){
        let byte = bytes[i] ^ bytes[maskStart + ((i - dataStart) % 4)]
        let msg = String.fromCharCode(byte);  
        decodedMessage = decodedMessage + msg;  
    }
    return decodedMessage;
}
