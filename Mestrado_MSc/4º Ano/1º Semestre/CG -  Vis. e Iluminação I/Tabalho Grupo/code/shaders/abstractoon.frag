#version 330

uniform sampler2D texture;

in Data {
	vec4 eye;
	vec2 texCoord;
	vec3 normal;
	vec3 l_dir;
} DataIn;

out vec4 colorOut;

#define HueLevCount 10
#define SatLevCount 12
#define ValLevCount 7

float[HueLevCount] HueLevels = float[] (  0.0,  20.0,  50.0,  85.0, 100.0,
                                        140.0, 160.0, 240.0, 300.0, 360.0);

float[SatLevCount] SatLevels = float[] ( 0.00,  0.08,  0.16,  0.24,   
                                         0.32,  0.40,  0.48,  0.56, 
                                         0.64,  0.72,  0.88,  1.00);

float[ValLevCount] ValLevels = float[] ( 0.0,   0.17,  0.34,  0.51,  0.68,  0.85,  1);

vec3 RGBtoHSV( float r, float g, float b) { 
   float minv, maxv, delta;
   vec3 res;

   minv = min(min(r, g), b);
   maxv = max(max(r, g), b);
   res.z = maxv;            // v
   
   delta = maxv - minv;

   if( maxv != 0.0 )
      res.y = delta / maxv;      // s
   else {
      // r = g = b = 0      // s = 0, v is undefined
      res.y = 0.0;
      res.x = -1.0;
      return res;
   }

   if( r == maxv )
      res.x = ( g - b ) / delta;      // between yellow & magenta
   else if( g == maxv )
      res.x = 2.0 + ( b - r ) / delta;   // between cyan & yellow
   else
      res.x = 4.0 + ( r - g ) / delta;   // between magenta & cyan

   res.x = res.x * 60.0;            // degrees
   if( res.x < 0.0 )
      res.x = res.x + 360.0;
      
   return res;

}

vec3 HSVtoRGB(float h, float s, float v ) 
{
   int i;
   float f, p, q, t;
   vec3 res;

   if( s == 0.0 ) 
   {
      // achromatic (grey)
      res.x = v;
      res.y = v;
      res.z = v;
      return res;
   }

   h /= 60.0;         // sector 0 to 5
   i = int(floor( h ));
   f = h - float(i);         // factorial part of h
   p = v * ( 1.0 - s );
   q = v * ( 1.0 - s * f );
   t = v * ( 1.0 - s * ( 1.0 - f ) );

   switch(i) 
   {
      case 0:
         res.x = v;
         res.y = t;
         res.z = p;
         break;
      case 1:
         res.x = q;
         res.y = v;
         res.z = p;
         break;
      case 2:
         res.x = p;
         res.y = v;
         res.z = t;
         break;
      case 3:
         res.x = p;
         res.y = q;
         res.z = v;
         break;
      case 4:
         res.x = t;
         res.y = p;
         res.z = v;
         break;
      default:      // case 5:
         res.x = v;
         res.y = p;
         res.z = q;
         break;
   }
   return res;
}

float nearestLevel(float col, int mode) 
{
   int levCount;
   if (mode==0) levCount = HueLevCount;
   if (mode==1) levCount = SatLevCount;
   if (mode==2) levCount = ValLevCount;
   
   for (int i =0; i<levCount-1; i++ ) {
     if (mode==0) {
        if (col >= HueLevels[i] && col <= HueLevels[i+1]) {
          return HueLevels[i+1];
        }
     }
     if (mode==1) {
        if (col >= SatLevels[i] && col <= SatLevels[i+1]) {
          return SatLevels[i+1];
        }
     }
     if (mode==2) {
        if (col >= ValLevels[i] && col <= ValLevels[i+1]) {
          return ValLevels[i+1];
        }
     }
   }
}

// averaged pixel intensity from 3 color channels
float avg_intensity(vec4 pix) 
{
 return (pix.r + pix.g + pix.b)/3.;
}

vec4 get_pixel(vec2 coords, float dx, float dy) 
{
 return texture2D(texture,coords + vec2(dx, dy));
}

// returns pixel color
float IsEdge(vec2 coords) {
    float dxtex = 1.0 /float(textureSize(texture,0)) ;
    float dytex = 1.0 /float(textureSize(texture,0));
    float pix[9];
    int k = -1;
    float delta;

    // read neighboring pixel intensities
    for (int i=-1; i<2; i++) {
      for(int j=-1; j<2; j++) {
        k++;
        pix[k] = avg_intensity(get_pixel(coords, float(i)*dxtex, float(j)*dytex));
      }
    }

    // average color differences around neighboring pixels
    delta = (abs(pix[1]-pix[7])+
             abs(pix[5]-pix[3]) +
             abs(pix[0]-pix[8]) +
             abs(pix[2]-pix[6]))/4.;

    return clamp(delta,0.0,1.0);
}

void main() {
    float shininess = 128;
    // normalize both input vectors
    vec3 n = normalize(DataIn.normal);
    vec3 e = normalize(vec3(DataIn.eye));

    //
    vec3 texture = texture2D(texture, DataIn.texCoord).rgb;
    vec3 cenaDia = RGBtoHSV(texture.r,texture.g,texture.b);

    cenaDia.x = nearestLevel(cenaDia.x, 0);
    cenaDia.y = nearestLevel(cenaDia.y, 1);
    cenaDia.z = nearestLevel(cenaDia.z, 2);

    float edg = IsEdge( DataIn.texCoord);
    float edge_thres = 50*0.001;

    vec3 cenaDia2 = (edg >= edge_thres)? vec3(0.0,0.0,0.0): HSVtoRGB(cenaDia.x,cenaDia.y,cenaDia.z);

    vec4 eColor = vec4(cenaDia2.x,cenaDia2.y,cenaDia2.z, 1);

    float intensity = dot(n,DataIn.l_dir);

    vec4 spec = vec4(0.0);
    if (intensity > 0.0){
        // compute the half vector
        vec3 h = normalize(DataIn.l_dir + e);
        // compute the specular intensity
        float intSpec = max(dot(h,n), 0.0);
        // compute the specular term into spec
        spec = vec4(1) * pow(intSpec,shininess);
    }

    colorOut = (intensity) * eColor + spec + 0.2*eColor;
}