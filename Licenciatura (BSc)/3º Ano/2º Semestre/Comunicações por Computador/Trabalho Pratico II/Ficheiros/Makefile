remover:
	rm -r -f *.class

compilar:
	javac *.java

dns:
	sudo /usr/sbin/named -c /home/core/primario/named.conf -g
	
reverse:
	java ReverseProxy

backend:
	java BackEndServer