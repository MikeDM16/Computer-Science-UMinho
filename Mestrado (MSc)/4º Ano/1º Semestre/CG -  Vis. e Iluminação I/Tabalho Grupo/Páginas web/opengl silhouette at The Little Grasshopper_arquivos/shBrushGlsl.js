// Author: Philip Rideout in April 2010

SyntaxHighlighter.brushes.glsl = function()
{
	
	var funcs = 
	'main fwidth cross dot normalize max pow';

	var keywords = 
	'attribute varying in out uniform ' +
	'void float int ivec2 ivec3 ivec4' +
	'vec2 vec3 vec4 ' +
	'mat2 mat3 mat4 ' +
	'break return while for if';
	
	this.regexList =
    [
        // Other css stuff: variable, value, constants, script
		{ regex: new RegExp('--[^\\[]{2}.*$', 'gmi'), css: 'preprocessor italic bold' },
		{ regex: new RegExp('\#[^\\[]{2}.*$', 'gmi'), css: 'preprocessor' },
		{ regex: new RegExp('\/\/[^\\[]{2}.*$', 'gmi'), css: 'comments' },
		{ regex: new RegExp('\\[\\[[^\\]]*\\]\\]', 'gmi'), css: 'string' },
		{ regex: new RegExp(this.getKeywords(funcs), 'gmi'), css: 'functions' },
        { regex: new RegExp(this.getKeywords(keywords), 'gmi'), css: 'keyword' }
	];

};

SyntaxHighlighter.brushes.glsl.prototype	= new SyntaxHighlighter.Highlighter();
SyntaxHighlighter.brushes.glsl.aliases	= ['glsl'];

