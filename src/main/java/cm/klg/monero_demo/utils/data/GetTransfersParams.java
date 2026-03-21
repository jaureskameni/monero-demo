package cm.klg.monero_demo.utils.data;

import java.util.List;

public record GetTransfersParams(boolean in, int account_index, List<Integer> subaddr_indices) {}
