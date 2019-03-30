#!/usr/bin/gawk -f

BEGIN {
	FS = "::";
	enc = "<html> <head> <meta charset='UTF-8' /> </head> </html>"
	fmt = "<li> <a href = '%s'> % </a></li>\n"
	evo = "http://natura.di.uminho.pt/~jj/PL17/"
}

!prov[$1]{
	print enc "<h1>" $1 </h1> > $1 ".html";
}

{
	printf(fmt, $(NF-1), $2) > %1".html";
	prov[$1]++;
}

END{
	print enc > "index.html";
	for(i in prov) printf(fmt, i ".html", i) > "index.html";
	print "</body> </html>" > "index.html";
}