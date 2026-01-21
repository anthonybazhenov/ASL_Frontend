<head>
    <title>Camera Capture</title>
</head>
<body>
    <video id="video" width="640" height="480" autoplay></video>
    <canvas id="canvas" style="display: none;"></canvas>
    <div id="timer">Next photo in: 2 seconds</div>
    <script>
        const video = document.getElementById('video');
        const canvas = document.getElementById('canvas');
        const timerElement = document.getElementById('timer');
        // access to camera
        navigator.mediaDevices.getUserMedia({ video: true })
            .then(function (stream) {
                video.srcObject = stream;
            })
            .catch(function (err) {
                console.log("An error occurred: " + err);
            });
        function captureAndSendImage() {
            // draw the current frame from the video onto the canvas
            canvas.width = video.videoWidth;
            canvas.height = video.videoHeight;
            canvas.getContext('2d').drawImage(video, 0, 0, canvas.width, canvas.height);
            // get base64 representation of the image data
            const imageData = canvas.toDataURL('image/png').replace(/^data:image\/\w+;base64,/, '');
            // send image data to backend
            fetch('http://localhost:8085/image', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ image: imageData }),
            })
            .then(response => response.json())
            .then(data => {
                console.log(data.message);
            })
            .catch(error => {
                console.error('Error:', error);
            });
        }
        // capture image every 2 seconds
        setInterval(function() {
            captureAndSendImage();
            updateTimer();
        }, 2000);
        function updateTimer() {
            let seconds = 2;
            timerElement.textContent = `Next capture in: ${seconds} seconds`;
            const countdown = setInterval(() => {
                seconds--;
                timerElement.textContent = `Next capture in: ${seconds} seconds`;
                if (seconds <= 0) {
                    clearInterval(countdown);
                }
            }, 1000);
        }
    </script>
</body>
