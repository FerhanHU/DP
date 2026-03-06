public class Main {
    static void main() {
        IReizigerDao rdao = new ReizigerDaoPsql();
        IAdresDao adoa = new AdresDaoPsql();

        rdao.setAdao(adao);
        adao.setRdao(rdao);
    }
}