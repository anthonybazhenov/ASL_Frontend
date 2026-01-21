---
layout: default
title: Data Format Converter
permalink: /dformat
---
<html>
<body>

<input type="file" id="imageInput" accept="image/*">
<br>
<canvas id="canvas" style="display:none;"></canvas>
<br>
<button id="convertButton">Convert to Pixel Brightness List</button>
<textarea id="pixelList" rows="10" cols="50"></textarea>

<script>
    var canvas = document.getElementById('canvas');
    var ctx = canvas.getContext('2d');
    var image = new Image();
    var grayscaleImageData;

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
        grayscaleImageData = ctx.getImageData(0, 0, 28, 28);
    }

    document.getElementById('convertButton').addEventListener('click', function() {
        var pixelValues = [];
        for (var i = 0; i < grayscaleImageData.data.length; i += 4) {
            pixelValues.push(grayscaleImageData.data[i]);
        }
        document.getElementById('pixelList').value = pixelValues.join(',');
    });
</script>

</body>
</html>
