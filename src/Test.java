
public class Test {

    public static void main(String[] args) throws Exception {
        Regex regex = new Regex("(f){2,3}e");
//        Regex regex = new Regex("(:ef|e)*as(:(b)+)");

        System.out.println("-----------showMatch-----------");
//        System.out.println(regex.match("efefeeasbbbooooooefefasbbbi"));
//        System.out.println(regex.match("easdabbbbi"));
//        System.out.println(regex.match("easdabcbcbcbc"));
//        System.out.println(regex.match("aottettawe"));

        System.out.println(regex.match("ffffeeffefffe"));
        System.out.println("-----------showMatch-----------");
    }

}
