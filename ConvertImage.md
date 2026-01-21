---
layout: default
title: ConvertImage
permalink: /cimage
---
<html>
<body>

<input type="file" id="imageInput" accept="image/*">
<br>
<canvas id="canvas"></canvas>
<br>
<button onclick="adjustBrightness(0.1)">Increase Brightness</button>
<button onclick="adjustBrightness(-0.1)">Decrease Brightness</button>

<script>
    var canvas = document.getElementById('canvas');
    var ctx = canvas.getContext('2d');
    var image = new Image();
    var grayscaleImageData;
    var currentBrightness = 1;

    document.getElementById('imageInput').addEventListener('change', function(event) {
        var reader = new FileReader();
        reader.onload = function() {
            image.src = reader.result;
        };
        reader.readAsDataURL(event.target.files[0]);
    });

    image.onload = function() {
        // Resize and draw image on canvas
        canvas.width = 28;
        canvas.height = 28;
        ctx.drawImage(image, 0, 0, 28, 28);

        // Convert to grayscale and store the image data
        convertToGrayscale();
        grayscaleImageData = ctx.getImageData(0, 0, 28, 28);
    };

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

    function adjustBrightness(change) {
        currentBrightness += change;
        ctx.putImageData(grayscaleImageData, 0, 0);
        ctx.filter = `brightness(${currentBrightness})`;
        ctx.drawImage(canvas, 0, 0, canvas.width, canvas.height);
    }
</script>

</body>
</html>