FROM fluent/fluentd:edge-debian
ADD conf/fluentd.conf /fluentd/etc/fluentd.conf
USER root
RUN ["gem", "install", "fluent-plugin-elasticsearch", "--no-document", "--version", "4.3.3"]
USER fluent