package ch.hsr.adv;

public class AdvApplication {

    AdvExtension extension;

    public void onDisplay() {
        if (extension != null){
            extension.print();
        }
    }

    //to shut up spotbugs =)
    public void doNothing(){
        extension = new AdvExtension() {
            @Override
            public void print() {

            }
        };
    }
}
