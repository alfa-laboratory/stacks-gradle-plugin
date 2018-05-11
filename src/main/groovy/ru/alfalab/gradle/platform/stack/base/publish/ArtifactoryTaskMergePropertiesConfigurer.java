package ru.alfalab.gradle.platform.stack.base.publish;

import org.gradle.api.Task;
import org.jfrog.build.extractor.clientConfiguration.ArtifactSpec;
import org.jfrog.build.extractor.clientConfiguration.ArtifactSpecs;
import org.jfrog.gradle.plugin.artifactory.task.ArtifactoryTask;

import java.util.List;

public class ArtifactoryTaskMergePropertiesConfigurer {
  private final ArtifactoryTask targetTask;
  private final ArtifactSpecs   userArtifactoryPublishSpecs;

  public ArtifactoryTaskMergePropertiesConfigurer(ArtifactoryTask targetTask) {
    this.targetTask = targetTask;
    this.userArtifactoryPublishSpecs = (ArtifactSpecs) targetTask.getArtifactSpecs().clone();
  }

  public void putAllSpecTo(List<ArtifactSpec> artifactSpecs) {

    targetTask.doFirst((Task task) -> {
      ArtifactoryTask artifactoryTask = (ArtifactoryTask) task;
      ArtifactSpecs   specs           = artifactoryTask.getArtifactSpecs();

      specs.addAll(userArtifactoryPublishSpecs);

      artifactSpecs.forEach(advancedArtifactSpec -> {
        if (!specs.contains(advancedArtifactSpec)) {
          artifactoryTask.getArtifactSpecs().add(advancedArtifactSpec);
        } else {
          specs.stream()
              .filter(artifactSpec -> artifactSpec.equals(advancedArtifactSpec))
              .forEach(artifactSpec -> advancedArtifactSpec.getProperties()
                  .forEach((key, value) -> artifactSpec.getProperties().putIfAbsent(key, value))
              );
        }
      });
    });

  }
}
