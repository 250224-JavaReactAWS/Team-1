/**
 * `HotelImageGallery` is a React functional component for displaying a gallery of hotel images.
 * 
 * This component allows users to view a series of images for a hotel. Users can navigate 
 * through the images using previous and next buttons.
 * 
 * Props:
 * - `images` (string[]): An array of image URLs to display in the gallery.
 * 
 * State:
 * - `currentIndex` (number): The index of the currently displayed image.
 * 
 * Methods:
 * - `prevImage`: Navigates to the previous image in the gallery. If the current image is the first, 
 *   it loops back to the last image.
 * - `nextImage`: Navigates to the next image in the gallery. If the current image is the last, 
 *   it loops back to the first image.
 * 
 * UI Elements:
 * - `Box`: A Material-UI container for layout and styling.
 * - `IconButton`: Buttons for navigating between images.
 * - `ArrowBackIosNewIcon`: Icon for the "previous" button.
 * - `ArrowForwardIosIcon`: Icon for the "next" button.
 * 
 * Behavior:
 * - If no images are provided, the component displays a placeholder with the text "No image available."
 * - If there is only one image, navigation buttons are not displayed.
 * 
 * Example Usage:
 * ```tsx
 * <HotelImageGallery images={["image1.jpg", "image2.jpg", "image3.jpg"]} />
 * ```
 */

import { Box, IconButton } from "@mui/material";
import ArrowBackIosNewIcon from "@mui/icons-material/ArrowBackIosNew";
import ArrowForwardIosIcon from "@mui/icons-material/ArrowForwardIos";
import { useState } from "react";

type Props = {
  images: string[];
};

export default function HotelImageGallery({ images }: Props) {
  const [currentIndex, setCurrentIndex] = useState(0);

  if (!images || images.length === 0) {
    return (
      <Box
        sx={{
          width: "100%",
          height: 200,
          backgroundColor: "#e0e0e0",
          display: "flex",
          alignItems: "center",
          justifyContent: "center",
          fontStyle: "italic",
        }}
      >
        No image available
      </Box>
    );
  }

  const prevImage = () =>
    setCurrentIndex((prev) => (prev === 0 ? images.length - 1 : prev - 1));
  const nextImage = () =>
    setCurrentIndex((prev) => (prev === images.length - 1 ? 0 : prev + 1));

  return (
    <Box sx={{ position: "relative", width: "100%", height: 200 }}>
      <Box
        component="img"
        src={images[currentIndex]}
        alt={`Hotel image ${currentIndex + 1}`}
        sx={{
          width: "100%",
          height: "100%",
          objectFit: "cover",
        }}
      />
      {images.length > 1 && (
        <>
          <IconButton
            onClick={prevImage}
            sx={{
              position: "absolute",
              top: "50%",
              left: 8,
              color: "white",
              transform: "translateY(-50%)",
            }}
          >
            <ArrowBackIosNewIcon />
          </IconButton>
          <IconButton
            onClick={nextImage}
            sx={{
              position: "absolute",
              top: "50%",
              right: 8,
              color: "white",
              transform: "translateY(-50%)",
            }}
          >
            <ArrowForwardIosIcon />
          </IconButton>
        </>
      )}
    </Box>
  );
}
