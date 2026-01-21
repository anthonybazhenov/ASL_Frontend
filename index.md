
<html>
<body>
    <div class="main">
        <div class="container">
            <div class="header">
                <img src="https://github.com/The-GPT-Warriors/ASLFrontend/assets/107821010/a767e359-1bb9-4fda-86f6-3e870d85938e" alt="logo" class="logo">
                <h1 class="title">Welcome to The GPT Warriors Organization!!</h1>
            </div>
            <div class="main">
                <h3 class="description1">Team Members - Tay Kim, Anthony Bazhenov, Ethan Tran, Emaad Mir</h3>
                <br>
                <div class="camera"></div>
                <input class="text" id="predictionInput" placeholder="Predictions from the model will go here" readonly>
                <div id="timer" style="color: black;">Next capture in: 2 seconds</div>
                <br>
                <br>
                <p class="description2">To get started, please log in or make an account.</p>
                <br>
                <button class="button1" onclick="window.location.href='{{site.baseurl}}/login'">Login</button>
                <button class="button2" onclick="window.location.href='{{site.baseurl}}/signup'">Create an Account</button>
        </div>
    </div>
  <script>
    const video = document.createElement('video');
    const canvas = document.createElement('canvas');
    const timerElement = document.getElementById('timer');
    const predictionInput = document.getElementById('predictionInput');
    const constraints = {
      video: true
    };
    navigator.mediaDevices.getUserMedia(constraints)
      .then((stream) => {
        video.srcObject = stream;
        video.onloadedmetadata = () => {
          video.play();
        };
        document.querySelector('.camera').appendChild(video);
      })
      .catch((err) => {
        console.log(err);
      });
    function captureAndSendImage() {
        // draw the current frame from the video onto the canvas
        canvas.width = video.videoWidth;
        canvas.height = video.videoHeight;
        canvas.getContext('2d').drawImage(video, 0, 0, canvas.width, canvas.height);
        // get base64 representation of the image data
        const imageData = canvas.toDataURL('image/png').replace(/^data:image\/\w+;base64,/, '');
        // send the image data to the backend
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
            // Display the prediction in the input field
            if (data.message) {
                predictionInput.value = data.message;
            } else if (data.prediction) {
                predictionInput.value = data.prediction;
            } else if (data.letter) {
                predictionInput.value = data.letter;
            }
        })
        .catch(error => {
            console.error('error:', error);
        });
    }
    // capture an image every 2 seconds
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
</html>

