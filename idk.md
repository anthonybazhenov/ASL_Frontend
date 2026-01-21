
      <div class="camera"></div>
      <input class="text" placeholder="Predictions from the model will go here" readonly>
      <div id="timer" style="color: black;">Next capture in: 2 seconds</div>
    </div>
    <div class="footer">
      <p class="footer-text">Powered by The GPT Warriors</p>
    </div>
  </div>
  <script>
    const video = document.createElement('video');
    const canvas = document.createElement('canvas');
    const timerElement = document.getElementById('timer');
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
