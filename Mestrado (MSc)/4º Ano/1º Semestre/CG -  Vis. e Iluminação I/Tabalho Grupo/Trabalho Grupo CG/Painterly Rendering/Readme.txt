**************************************************************************
**	Painterly Rendering
**
**	www.paulsprojects.net
**
**	paul@paulsprojects.net
**************************************************************************

Description:

 Beginning from a source photograph, this demo will create an image with a hand-painted appearance. It can do this in three distinct styles, "Impressionist", "Expressionist" and "Pointillist".

The painting is generated using multiple layers. The first layer is painted with a large brush, and each further layer uses a smaller brush than the previous. Each time, the photo is blurred using a Gaussian filter and this is then approximated by the brush strokes. If the previous layer is "close enough" to this reference within a given area, no painting occurs there. This means that large areas with little color variation will only receive brush strokes from the larger brushes, wheras more detailed areas will receive finer painting. The brush strokes are rendered as cubic B-splines which follow the gradients within the image (calculated using a Sobel filter). These are tesselated manually and drawn by tracing the path of a curve with a circle.

The "Impressionist" style generates a painting which attempts to convey the scene within the photo realistically. Few restrictions are placed on the brush strokes used. An example of this is the zebra picture above.

The "Expressionist" style uses more restrictions, in particular, long brush strokes must be used. The colors of the painting are also enhanced. This produces paintings like that of the church shown here.

The "Pointillist" style is similar to the "Impressionist", but brush strokes are limited - they must be extremely short.

The generation of a single painting takes approximately 12 seconds (Athlon XP 2000, GeForce FX 5200). Three source images are provided and each is painted in the three styles, so the program requires approximately 2 minutes before any results are shown. You are then free to browse between all three images and all styles. You can also view the original source images for comparison.

The source images provided were found by a simple web search. If one belongs to you and you would rather it was not used here, please let me know and I will remove the offending image. 


Requirements:

Either EXT_texture_rectangle or NV_texture_rectangle.


References:

"Painterly Rendering with Curved Brush Strokes of Multiple Sizes", Aaron Hertzmann, New York University.
"Focus on Curves and Surfaces", Kelly Dempski, Premier Press.


Keys:

F1		-	Take a screenshot
Escape	-	Quit

Z		-	Use zebra source image
C		-	Use church source image
L		-	Use lillies source image

1		-	Show source image
2		-	Show "Impressionist" painting
3		-	Show "Expressionist" painting
4		-	Show "Pointillist" painting
