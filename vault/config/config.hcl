ui            = true
cluster_addr  = "http://127.0.0.1:8201"
api_addr      = "http://127.0.0.1:8200"
disable_mlock = true
default_lease_ttl= "876000h"
max_lease_ttl= "876000h"

log_level= "info"

storage "file" {
  path = "/vault/file"
  node_id = "file_node"
}

listener "tcp" {
  address       = "0.0.0.0:8200"
  tls_disable = 1
}
