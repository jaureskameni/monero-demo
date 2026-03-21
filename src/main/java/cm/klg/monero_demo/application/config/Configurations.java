package cm.klg.monero_demo.application.config;

public interface Configurations {
  String url();

  String username();

  String password();

  boolean enabled();

  int accountIndex();

  String rpcVersion();

  String rpcId();
}
