package dev.natsuume.knp4j.data.define;

import java.util.List;

public interface KnpNode {
  String getSurfaceForm();
  String getReadingForm();
  List<KnpFeature> getFeatures();
}
