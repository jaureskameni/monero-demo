package cm.klg.monero_demo.utils;

public enum RpcMethode {
  // Gestion des adresses
  CREATE_ADDRESS("create_address"),
  GET_ADDRESS("get_address"),
  GET_ADDRESSES("get_addresses"),

  // Gestion du solde
  GET_BALANCE("get_balance"),

  // Gestion des transactions
  GET_TRANSFERS("get_transfers"),
  TRANSFER("transfer"),

  // Autres méthodes utiles
  GET_HEIGHT("get_height"),
  REFRESH("refresh"),
  SWEEP_ALL("sweep_all");

  private final String methodName;

  RpcMethode(String methodName) {
    this.methodName = methodName;
  }

  public String getMethodName() {
    return methodName;
  }

  @Override
  public String toString() {
    return methodName;
  }
}
