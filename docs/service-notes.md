# AutoLoc — Note Technique : Couche Service, IoC et Injection de Dépendances
**Atelier 4 — UP ASI : Architecture des Systèmes d'Information**

---

## A. Modes d'injection comparés

| Critère | Injection par constructeur | Injection par attribut (`@Autowired`) |
| :--- | :--- | :--- |
| **Champ `final` possible ?** | **Oui** (garantit l'immuabilité) | **Non** (le champ doit être modifiable par réflexion) |
| **Dépendance visible dans la signature ?** | **Oui** (explicite dans les paramètres du constructeur) | **Non** (dépendance cachée, constructeur par défaut vide) |
| **Utilisation hors conteneur sans `null` ?** | **Oui** (`new Service(repoMock)` instancie un objet complet) | **Non** (`new Service()` laisse les dépendances à `null`) |
| **Signalé par SonarQube / SonarLint ?** | **Non** (conforme aux bonnes pratiques) | **Oui** (règle **S6813** : *Avoid field injection*) |

---

## B. Couche Service

| Service | Dépendances injectées | Mode | Justification |
| :--- | :--- | :--- | :--- |
| `AgenceServiceImpl` | `IAgenceRepository` | Constructeur (`@RequiredArgsConstructor`) | Dépendance obligatoire, champ `final`, abstraction via interface |
| `ClientServiceImpl` | `IClientRepository` | Constructeur (`@RequiredArgsConstructor`) | Dépendance obligatoire, champ `final`, abstraction via interface |
| `ContratServiceImpl` | `IContratRepository` | Constructeur (`@RequiredArgsConstructor`) | Dépendance obligatoire, champ `final`, abstraction via interface |
| `EmployeServiceImpl` | `IEmployeRepository` | Constructeur (`@RequiredArgsConstructor`) | Dépendance obligatoire, champ `final`, abstraction via interface |
| `EquipementServiceImpl` | `IEquipementRepository` | Constructeur (`@RequiredArgsConstructor`) | Dépendance obligatoire, champ `final`, abstraction via interface |
| `MaintenanceServiceImpl` | `IMaintenanceRepository` | Constructeur (`@RequiredArgsConstructor`) | Dépendance obligatoire, champ `final`, abstraction via interface |
| `PaiementServiceImpl` | `IPaiementRepository` | Constructeur (`@RequiredArgsConstructor`) | Dépendance obligatoire, champ `final`, abstraction via interface |
| `ReservationServiceImpl` | `IReservationRepository` | Constructeur (`@RequiredArgsConstructor`) | Dépendance obligatoire, champ `final`, abstraction via interface |
| `VehiculeServiceImpl` | `IVehiculeRepository` | Constructeur (`@RequiredArgsConstructor`) | Dépendance obligatoire, champ `final`, abstraction via interface |

---

## C. Diagnostic des messages d'erreur du conteneur

### Message 1
* **Exception :** `NoSuchBeanDefinitionException` (encapsulée dans `UnsatisfiedDependencyException`).
* **Bean concerné :** `IContratService` requis par `ContratController`.
* **Causes probables :**
  1. L'annotation `@Service` ou `@Component` a été omise sur la classe `ContratServiceImpl`.
  2. La classe `ContratServiceImpl` est située dans un package non scanné par `@SpringBootApplication`.
* **Correction :** Ajouter l'annotation `@Service` sur `ContratServiceImpl` et s'assurer qu'elle se trouve sous l'arborescence du package racine de l'application.

### Message 2
* **Exception :** `NoUniqueBeanDefinitionException`.
* **Bean concerné :** `NotificationController` qui demande un bean unique mais deux candidats sont trouvés (`emailNotificateur` et `smsNotificateur`).
* **Correction 1 (`@Primary`) :** Ajouter l'annotation `@Primary` sur la classe du bean préféré (ex. `@Primary` sur la classe `EmailNotificateur`).
* **Correction 2 (`@Qualifier`) :** Utiliser l'annotation `@Qualifier("emailNotificateur")` directement sur le paramètre du constructeur de `NotificationController`.

### Message 3
* **Exception :** `BeanCurrentlyInCreationException` (dépendance circulaire entre `clientServiceImpl` et `reservationServiceImpl`).
* **Pourquoi l'injection par constructeur rend-elle ce cycle impossible à contourner ?**
  Parce que pour instancier un bean A via son constructeur, Spring doit au préalable avoir instancié toutes ses dépendances. Si A nécessite B et B nécessite A, aucun des deux constructeurs ne peut être invoqué en premier, bloquant immédiatement le cycle de vie à l'étape d'instanciation.
* **Piste de conception :** Extraire la logique ou la coordination commune dans un service intermédiaire tiers (par exemple un service de réservation de haut niveau), ou repenser l'architecture pour rendre la relation unidirectionnelle.

---

## D. Anomalies SonarQube for IDE (SonarLint)

1. **Règle S6813 (*Avoid field injection*) :**
   * *Explication :* L'injection par attribut (`@Autowired` sur champ privé) cache les dépendances, empêche les champs d'être `final` et rend la classe impossible à instancier correctement en dehors du conteneur Spring (tests unitaires avec `new`).
   * *Correction :* Remplacement par l'injection par constructeur avec Lombok `@RequiredArgsConstructor` et champs `private final`.

2. **Règle S112 (*Generic exceptions should never be thrown*) :**
   * *Explication :* Lever une exception générique comme `RuntimeException` ou `Exception` ne permet pas à l'appelant de distinguer la cause exacte de l'erreur ni d'appliquer un traitement adapté.
   * *Correction :* Création et utilisation d'une exception métier dédiée `ResourceNotFoundException`.

3. **Règle S1144 / Nettoyage des imports & absence de `@Data` sur entités JPA :**
   * *Explication :* Utiliser `@Data` sur des entités JPA génère automatiquement des méthodes `equals`, `hashCode` et `toString` incluant les relations bidirectionnelles, provoquant des boucles infinies (`StackOverflowError`) ou des chargements inattendus (`LazyInitializationException`).
   * *Correction :* Utilisation ciblée de `@Getter`, `@Setter`, `@NoArgsConstructor`, `@AllArgsConstructor` sans `@Data`.

---

## E. Questions de compréhension

1. **Où est le `new` de `ContratServiceImpl` et qui l'exécute ?**
   Il n'y a aucun `new ContratServiceImpl(...)` écrit par le développeur dans le code applicatif. C'est le conteneur Spring IoC (`ApplicationContext` / `BeanFactory`) qui instancie la classe par introspection/réflexion lors de la phase d'instanciation des beans au démarrage de l'application, après avoir détecté l'annotation `@Service` via le scan de composants.

2. **Pourquoi un contrôleur dépendra-t-il de `IContratService` et non de `ContratServiceImpl` ?**
   Pour respecter le principe d'inversion des dépendances (DIP de SOLID) et assurer un faible couplage. Le contrôleur s'appuie sur une abstraction (l'interface), ce qui permet de remplacer facilement l'implémentation (par exemple un mock lors des tests unitaires), et permet à Spring d'insérer des proxys dynamiques (gestion des transactions, sécurité, métriques) sans impacter le contrôleur.

3. **Pourquoi un service singleton doit-il rester sans état ? Donner un exemple de bug.**
   Dans Spring, les beans singleton sont partagés entre tous les threads traitant les requêtes HTTP concurrentes. Si un service conserve un état mutable (ex. un attribut `private Contrat contratEnCours;`), deux requêtes exécutées simultanément vont modifier et écraser cet attribut partagé, provoquant des conditions de concurrence (*race conditions*), des incohérences de données et des fuites de données entre utilisateurs.

4. **Pourquoi `update` charge-t-il l'entité existante avant d'appeler `save` au lieu de lui passer directement l'objet reçu ?**
   Passer directement l'objet reçu à `save` écraserait toutes les colonnes de la table par les valeurs reçues (mettant à `null` les champs omis) et, en présence de cascades ou d'`orphanRemoval`, risquerait de détruire des collections liées (ex. paiements, réservations). En rechargeant l'entité existante, on ne met à jour que les attributs modifiables autorisés tout en préservant l'intégrité de l'identifiant et des relations existantes.

5. **Quelle différence entre `@Component` et `@Bean` ? Entre `@Primary` et `@Qualifier` ? Pourquoi `IPaiementService` n'expose-t-il ni création, ni modification, ni suppression ?**
   * **`@Component` vs `@Bean` :** `@Component` est une annotation de classe détectée automatiquement par le scan de composants. `@Bean` est une annotation de méthode dans une classe `@Configuration`, utilisée pour déclarer explicitement un bean (indispensable pour les classes de bibliothèques tierces non modifiables).
   * **`@Primary` vs `@Qualifier` :** `@Primary` est posé sur la classe d'un bean pour en faire le choix par défaut en cas d'ambiguïté. `@Qualifier("nomDuBean")` est posé au point d'injection pour désigner explicitement le bean précis souhaité.
   * **`IPaiementService` :** Un paiement est intrinsèquement lié à un contrat (composition forte). Les opérations de création, modification ou annulation d'un paiement doivent être pilotées par le cycle de vie du contrat pour garantir la cohérence comptable et contractuelle.
