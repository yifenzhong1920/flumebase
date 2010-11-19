// (c) Copyright 2010 Odiago, Inc.

package com.odiago.rtengine.exec.local;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cloudera.flume.core.Event;

import com.odiago.rtengine.exec.FlowElementContext;
import com.odiago.rtengine.exec.FlowElementImpl;

import com.odiago.rtengine.flume.EmbeddedFlumeConfig;
import com.odiago.rtengine.flume.EmbeddedNode;

/**
 * FlowElement providing source data from a local Flume sink.
 */
public class LocalFlumeSinkElement extends FlowElementImpl {
  private static final Logger LOG = LoggerFactory.getLogger(
      LocalFlumeSinkElement.class.getName());

  /** Flume's name for this logical node. */
  private String mFlowSourceId;

  /** The manager of the embedded Flume node instances. */
  private EmbeddedFlumeConfig mFlumeConfig;

  /** The Flume logical node resources associated with this FlowElement. */
  private EmbeddedNode mEmbeddedFlumeNode;

  /**
   * The EventSource that we couple to our internal sink for
   * this logical node.
   */
  private String mDataSource;

  public LocalFlumeSinkElement(FlowElementContext context, String flowSourceId,
      EmbeddedFlumeConfig flumeConfig, String dataSource) {
    super(context);

    mFlowSourceId = flowSourceId;
    mFlumeConfig = flumeConfig;
    mDataSource = dataSource;
  }

  @Override
  public void open() throws IOException {
    mEmbeddedFlumeNode = new EmbeddedNode(mFlowSourceId, getContext(), mFlumeConfig,
        mDataSource);
    mEmbeddedFlumeNode.open();
  }

  public void close() throws IOException, InterruptedException {
    mEmbeddedFlumeNode.close();
  }

  public void takeEvent(Event e) {
    // We generate our own events; nothing should be upstream from us.
    throw new RuntimeException("LocalFlumeSinkElement does not support incoming events");
  }

  @Override
  public String toString() {
    return "FlumeSink[mFlowSourceId=\"" + mFlowSourceId + "\", "
        + "mDataSource=\"" + mDataSource + "\"]";
  }
}
