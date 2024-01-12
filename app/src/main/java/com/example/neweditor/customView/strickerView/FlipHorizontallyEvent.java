package com.example.neweditor.customView.strickerView;

/**
 * @author wupanjie
 */

public class FlipHorizontallyEvent extends AbstractFlipEvent {

  @Override @StickerView.Flip protected int getFlipDirection() {
    return  StickerView.FLIP_HORIZONTALLY;
  }
}
