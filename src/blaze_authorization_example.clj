(ns blaze-authorization-example
  (:require
   [integrant.core :as ig]
   [taoensso.timbre :as log]))


(defn wrap-authorization
  "Authorize if FHIR type is patient, otherwise unauthorized."
  [conn]
  (fn [handler]
    (fn [request]
      (let [fhir-type (get-in request [:reitit.core/match :data :fhir.resource/type])]
        (if (= fhir-type "Patient")
          (handler request)
          {:status 403 :body "Unauthorized"})))))


(defmethod ig/init-key :blaze-authorization-example/authorization
  [_ {:database/keys [conn]}]
  (log/info "Init authorization")
  (wrap-authorization conn))
