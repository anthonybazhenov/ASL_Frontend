
# Image Pixel Brightness Extractor

This document describes how to process a 28x28 greyscale image to output the brightness of all 784 pixels in a list separated by commas.

## Requirements

- A 28x28 greyscale image file.

## Process

1. Ensure your 28x28 greyscale image is ready for processing.
2. Use an image processing tool or script to read the image.
3. Convert the image into a 28x28 pixel matrix, if not already in this format.
4. For each pixel, extract its brightness level. In a greyscale image, this can be the value of the pixel itself, as greyscale values range from 0 (black) to 255 (white).
5. Compile the brightness levels of all 784 pixels into a list, starting from the top-left corner and moving horizontally first, then down to the next line.
6. Format the list as `label,pixel1,pixel2,...,pixel784`, where `label` is an optional identifier for the image or the target variable in machine learning datasets.

## Example

Given a 28x28 greyscale image, the output should look like this:

```
label,pixel1,pixel2,pixel3,...,pixel784
6,149,149,150,...,107
```

The first value (`6` in this example) is the label or identifier. Each `pixelX` value represents the brightness of a pixel, starting from the top-left corner of the image.

## Usage

This format is commonly used in machine learning and data analysis for image classification tasks, especially for handwritten digit recognition like the MNIST dataset.
