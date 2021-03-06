/*
 * Copyright 2018 ThoughtWorks, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.thoughtworks.go.apiv1.pipelineselection.representers;

import com.thoughtworks.go.api.base.OutputWriter;
import com.thoughtworks.go.api.representers.JsonReader;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PipelineSelectionsRepresenter {
    public static void toJSON(OutputWriter writer, PipelineSelectionResponse pipelineSelectionResponse) {
        writer.addChildList("selections", pipelineSelectionResponse.selections())
                .add("blacklist", pipelineSelectionResponse.blacklist())
                .addChild("pipelines", pipelineGroupsWriter -> {
                    pipelineSelectionResponse.getPipelineConfigs().forEach(pipelineConfigs -> {
                        List<String> pipelineNames = pipelineConfigs
                                .getPipelines().stream()
                                .map(pipelineConfig -> pipelineConfig.getName().toString())
                                .collect(Collectors.toList());
                        if (!pipelineNames.isEmpty()) {
                            pipelineGroupsWriter.addChildList(pipelineConfigs.getGroup(), pipelineNames);
                        }
                    });
                });
    }

    public static PipelineSelectionResponse fromJSON(JsonReader reader) {
        List<String> selections = reader.readStringArrayIfPresent("selections").orElse(Collections.emptyList());
        Boolean blacklist = reader.optBoolean("blacklist").orElse(true);

        return new PipelineSelectionResponse(selections, blacklist, null);
    }


}
