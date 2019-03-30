#version 330

uniform sampler2D texture;
uniform float edgeSize;

in Data {
	vec4 eye;
	vec2 texCoord;
	vec3 normal;
	vec3 l_dir;
} DataIn;

out vec4 colorOut;


// averaged pixel intensity from 3 color channels
float avg_intensity(vec4 pix) 
{
 return (pix.r + pix.g + pix.b)/3.;
}

vec4 get_pixel(vec2 coords, float dx, float dy) 
{
 return texture2D(texture, coords + vec2(dx, dy));
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
    vec3 texture_aux = texture2D(texture, DataIn.texCoord).rgb;

    float edg = IsEdge(DataIn.texCoord);

    texture_aux = (edg >= edgeSize)? vec3(0.0,0.0,0.0):texture_aux;

    vec4 eColor = vec4(texture_aux, 1);

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