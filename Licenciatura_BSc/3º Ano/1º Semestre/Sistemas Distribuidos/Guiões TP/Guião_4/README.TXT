    final int N;
   int proc = 0;
   int stage = 0;
   
   public Barreira(int n){this.N = n; }
   
   public synchronized void esperar() throws InterruptedException 
   {
       int local = stage;
       proc++;
       if(proc < N){
           while(local == stage){
               wait();
            }
       }else{
           stage++;
           proc = 0;
           notifyAll();
        }
   }