---
layout: default
title: ConvertVideoFrames
permalink: /cvideo
---
<html>
<body>

<input type="file" id="videoInput" accept="video/*">
<br>
<canvas id="canvas"></canvas>
<br>
<button onclick="processVideo()">Process Video</button>

<script>
    var canvas = document.getElementById('canvas');
    var ctx = canvas.getContext('2d');
    var video = document.createElement('video');
    var currentFrameImageData;

    document.getElementById('videoInput').addEventListener('change', function(event) {
        var file = event.target.files[0];
        var url = URL.createObjectURL(file);
        video.src = url;
        video.load();
        video.addEventListener('loadeddata', function() {
            processVideo();
        });
    });

    function processVideo() {
        // Set canvas size to desired frame size (200x200 in this case)
        canvas.width = 200;
        canvas.height = 200;

        video.currentTime = 0; // Start from the beginning of the video
        video.addEventListener('seeked', extractFrame);
    }

    function extractFrame() {
        if (video.ended || video.currentTime >= video.duration) {
            console.log('Video processing completed.');
            return;
        }

        // Draw current frame on canvas
        ctx.drawImage(video, 0, 0, canvas.width, canvas.height);

        // Convert current frame to grayscale
        convertToGrayscale();
        
        // Get grayscale image data
        currentFrameImageData = ctx.getImageData(0, 0, canvas.width, canvas.height);

        // Example of how to process pixel data
        // processPixelData(currentFrameImageData);

        // Move to the next frame. Adjust the value as needed to skip frames or slow down the frame extraction
        video.currentTime += 0.033; // Assuming 30fps; adjust accordingly
    }

    function convertToGrayscale() {
        var imageData = ctx.getImageData(0, 0, canvas.width, canvas.height);
        for (var i = 0; i < imageData.data.length; i += 4) {
            var avg = (imageData.data[i] + imageData.data[i + 1] + imageData.data[i + 2]) / 3;
            imageData.data[i] = avg; // Red
            imageData.data[i + 1] = avg; // Green
            imageData.data[i + 2] = avg; // Blue
        }
        ctx.putImageData(imageData, 0, 0);
    }

    // Placeholder for processing pixel data
    function processPixelData(imageData) {
        // Process the pixel data, for example, logging or converting to a specific format
        console.log("Process pixel data here.");
        // Loop through imageData.data and handle each pixel's value
    }
</script>

</body>
</html>
